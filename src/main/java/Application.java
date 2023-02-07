import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;

public class Application extends JFrame {
    private JTextField stepTextField;
    private JTextField minTextField;
    private JTextField maxTextField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton calculateButton;
    private JTextField result;
    private JTable table;
    private JPanel rootPanel;

    private final DefaultTableModel defaultTableModel;
    private static final String[] tableHeader = {"step", "min", "max", "result"};

    public Application() {
        super("Lab_1");
        setContentPane(rootPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setSize(800, 600);
        defaultTableModel = (DefaultTableModel) table.getModel();
        Arrays.stream(tableHeader).forEach(defaultTableModel::addColumn);

        addButton.addActionListener(new AddButtonActionListener());
        deleteButton.addActionListener(new DeleteButtonActionListener());
        calculateButton.addActionListener(new CalculateButtonActionListener());

    }

    private void createUIComponents() {
        table = new JTable(){
            @Override
            public boolean isCellEditable(int row,int column){
                return column != 3;
            }
        };
    }

    private class AddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] row = new String[3];
            if ((!stepTextField.getText().isEmpty() && stepTextField.getText().matches("-?\\d*+(\\.\\d+)?")) &&
                    (!minTextField.getText().isEmpty() && minTextField.getText().matches("-?\\d*+(\\.\\d+)?")) &&
                    (!maxTextField.getText().isEmpty() && maxTextField.getText().matches("-?\\d*+(\\.\\d+)?"))) {
                row[0] = stepTextField.getText();
                stepTextField.setText("");
                row[1] = minTextField.getText();
                minTextField.setText("");
                row[2] = maxTextField.getText();
                maxTextField.setText("");
                defaultTableModel.addRow(row);
            }
        }
    }

    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                defaultTableModel.removeRow(selectedRow);
            }
        }
    }
    private class CalculateButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                return;
            }
            Vector args = defaultTableModel.getDataVector().get(selectedRow);
            double step = Double.parseDouble((String) args.get(0));
            double min = Double.parseDouble((String) args.get(1));
            double max = Double.parseDouble((String) args.get(2));
            double inResult = 0;

            if (Math.abs(min) == Math.abs(max)) {
                result.setText("0");
                defaultTableModel.setValueAt("0", selectedRow, 3);
                return;
            }
            if (min == 0.0 || max == 0.0) {
                result.setText("Интеграл расходится");
                defaultTableModel.setValueAt("Интеграл расходится", selectedRow, 3);
                return;
            }
            for (double i = min; i <= max - step; i += step) {
                if (i != 0) {
                    inResult += (1 / i + 1 / (i + step)) * step / 2;
                    defaultTableModel.setValueAt(inResult, selectedRow, 3);
                }
            }
            result.setText(String.valueOf(inResult));

        }
    }

    public static void main(String[] args) {
        new Application();
    }

}
