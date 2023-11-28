package Actividad6EverthPOO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class Interface extends JFrame implements ActionListener {
    private JButton createBtn, readBtn, deleteBtn, updateBtn, clearBtn, exitBtn;
    private JTextField nameFld, numberFld;
    private JTextArea resultFld;
    private File Agenda = new File("Agenda.txt");
    public Interface() {
        setTitle("Agenda de contactos");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JLabel nameLabel = new JLabel("Ingrese Nombre");
        nameFld = new JTextField(10);
        JLabel numberLabel = new JLabel("Ingrese Número:");
        numberFld = new JTextField(10);
        createBtn = new JButton("Nuevo contacto");
        createBtn.addActionListener(this);
        readBtn = new JButton("Mostrar contactos");
        readBtn.addActionListener(this);
        updateBtn = new JButton("Modificar contacto");
        updateBtn.addActionListener(this);
        deleteBtn = new JButton("Borrar contacto");
        deleteBtn.addActionListener(this);
        clearBtn = new JButton("Limpiar campos");
        clearBtn.addActionListener(this);
        exitBtn = new JButton("Exit");
        exitBtn.addActionListener(this);
        resultFld = new JTextArea(15, 20);
        resultFld.setEditable(false);
        setLayout(new BorderLayout(10, 10));
        JPanel datosPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        datosPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
        datosPanel.add(nameLabel);
        datosPanel.add(nameFld);
        datosPanel.add(numberLabel);
        datosPanel.add(numberFld);
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botonesPanel.add(createBtn);
        botonesPanel.add(readBtn);
        botonesPanel.add(updateBtn);
        botonesPanel.add(deleteBtn);
        botonesPanel.add(clearBtn);
        botonesPanel.add(exitBtn);
        JPanel responsePanel = new JPanel(new GridLayout(1, 2, 5, 5));
        responsePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        JScrollPane scrollPane = new JScrollPane(resultFld);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        responsePanel.add(scrollPane);
        add(datosPanel, BorderLayout.NORTH);
        add(botonesPanel, BorderLayout.CENTER);
        add(responsePanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       //CREAR CONTACTO
        if (e.getActionCommand().equals("Nuevo contacto")) {
            String newName = nameFld.getText();
            long newNumber;
            try {
                newNumber = Long.parseLong(numberFld.getText());
            } catch (NumberFormatException ex) {
                resultFld.setText("Ingresa un número válido.");
                return;
            }
            try {
                File file = new File("Agenda.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                boolean found = false;
                while (raf.getFilePointer() < raf.length()) {
                    String nameNumberString = raf.readLine();
                    String[] lineSplit = nameNumberString.split("!");
                    if (lineSplit.length >= 2) {
                        String name = lineSplit[0];
                        long number = Long.parseLong(lineSplit[1]);

                        if (name.equals(newName) || number == newNumber) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    String nameNumberString = newName + "!" + String.valueOf(newNumber);
                    raf.writeBytes(nameNumberString);
                    raf.writeBytes(System.lineSeparator());
                    raf.close();
                    resultFld.setText("Contacto añadido: " + newName);
                } else {
                    raf.close();
                    resultFld.setText("El contacto ya existe.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                resultFld.setText("Error.");
            }
        }
        else if (e.getActionCommand().equals("Mostrar contactos")) {
            try {
                // LEER Y MOSTRAR CONTACTOS
                StringBuilder contactList = new StringBuilder("Lista de Contactos:\n\n");
                File file = new File("Agenda.txt");
                if (!file.exists()) {
                    resultFld.setText("No se encontraron contactos.");
                    return;
                }
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                while (raf.getFilePointer() < raf.length()) {
                    String nameNumberString = raf.readLine();
                    String[] lineSplit = nameNumberString.split("!");
                    String name = lineSplit[0];
                    long number = Long.parseLong(lineSplit[1]);
                    contactList.append("Ingrese Nombre ").append(name).append("\n");
                    contactList.append("Ingrese Número: ").append(number).append("\n\n");
                }
                resultFld.setText(contactList.toString());
                raf.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                resultFld.setText("Error.");
            } catch (NumberFormatException nef) {
                nef.printStackTrace();
                resultFld.setText("Error.");
            }
        }
        else if (e.getActionCommand().equals("Modificar contacto")) {
            // ACTUALIZAR EL CONTACTO
            String newName = nameFld.getText();
            long newNumber;
            try {
                newNumber = Long.parseLong(numberFld.getText());
            } catch (NumberFormatException ex) {
                resultFld.setText("Ingrese un número válido.");
                return;
            }
            try {
                File file = new File("Agenda.txt");
                if (!file.exists()) {
                    resultFld.setText("No se encontró el contacto.");
                    return;
                }
                BufferedReader reader = new BufferedReader(new FileReader(file));
                List<String> updatedLines = new ArrayList<>();
                String line;
                boolean found = false;
                while ((line = reader.readLine()) != null) {
                    String[] lineSplit = line.split("!");
                    String name = lineSplit[0];
                    long number = Long.parseLong(lineSplit[1]);
                    if (name.equals(newName)) {
                        found = true;
                        updatedLines.add(newName + "!" + newNumber);
                    } else {
                        updatedLines.add(line);
                    }
                }
                reader.close();
                if (!found) {
                    resultFld.setText("Contacto no encontrado: " + newName);
                } else {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
                    for (String updatedLine : updatedLines) {
                        writer.write(updatedLine);
                        writer.newLine();
                    }
                    writer.close();
                    resultFld.setText("Contacto actualizado: " + newName);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                resultFld.setText("Error.");
            }
        }
        else if (e.getActionCommand().equals("Borrar contacto")) {
            // ELIMINAR CONTACTO
            String nameToDelete = nameFld.getText();
            boolean found = false;
            try {
                File file = new File("Agenda.txt");
                if (!file.exists()) {
                    resultFld.setText("No se encontraron contactos.");
                    return;
                }
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                List<String> remainingLines = new ArrayList<>();
                String line;
                while ((line = raf.readLine()) != null) {
                    String[] lineSplit = line.split("!");
                    if (lineSplit.length >= 1) {
                        String name = lineSplit[0];
                        if (name.equals(nameToDelete)) {
                            found = true;
                        } else {
                            remainingLines.add(line);
                        }
                    }
                }
                raf.close();
                if (!found) {
                    resultFld.setText("Contacto no encontrado: " + nameToDelete);
                } else {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
                    for (String updatedLine : remainingLines) {
                        writer.write(updatedLine);
                        writer.newLine();
                    }
                    writer.close();
                    resultFld.setText("Contacto eliminado: " + nameToDelete);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                resultFld.setText("Error.");
            }
        }
        else if (e.getActionCommand().equals("Limpiar campos")) {
            nameFld.setText("");
            numberFld.setText("");
            resultFld.setText("");
        }
        else if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
    }
    private void guardarContacto() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Agenda))) {
            List<Contacto> contacts= new ArrayList<>();
            oos.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }}
    private void listarContactos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Agenda))) {
            List<Contacto> contacts = (List<Contacto>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
