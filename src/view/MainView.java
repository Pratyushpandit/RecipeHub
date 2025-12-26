package view;

import controller.RecipeController;
import entity.Recipe;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Queue;

public class MainView {
    private JFrame frame;
    private JTable recipeTable;
    private DefaultTableModel tableModel;
    private JPanel recentPanel;
    private JLabel statsLabel;

    public MainView(RecipeController controller) {
        initComponents(controller);
    }

    private void initComponents(RecipeController controller) {
        frame = new JFrame("RecipeHub - Personal Recipe Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Top: Welcome + Recent Recipes 
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to RecipeHub!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(welcomeLabel, BorderLayout.NORTH);

        recentPanel = new JPanel();
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.X_AXIS));
        recentPanel.setBorder(BorderFactory.createTitledBorder("Recently Added (Last 5)"));
        topPanel.add(new JScrollPane(recentPanel), BorderLayout.CENTER);

        // Center: Recipe Table 
        String[] columns = {"Title", "Cuisine", "Cooking Time (mins)", "Servings"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        recipeTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(recipeTable);

        // Bottom: Controls
        JPanel controlPanel = new JPanel();
        JButton addBtn = new JButton("Add Recipe");
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton refreshBtn = new JButton("Refresh");

        final JTextField searchField = new JTextField(20); 
        JButton searchBtn = new JButton("Search");

        JRadioButton ascBtn = new JRadioButton("Asc");
        JRadioButton descBtn = new JRadioButton("Desc");
        ButtonGroup sortGroup = new ButtonGroup();
        sortGroup.add(ascBtn);
        sortGroup.add(descBtn);
        JButton sortBtn = new JButton("Sort by Time");

        ascBtn.setSelected(true); // default

        // Stats
        statsLabel = new JLabel("Total Recipes: 0 | Avg Time: 0 mins");
        controlPanel.add(statsLabel);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);
        controlPanel.add(searchBtn);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(new JLabel("Sort:"));
        controlPanel.add(ascBtn);
        controlPanel.add(descBtn);
        controlPanel.add(sortBtn);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(addBtn);
        controlPanel.add(editBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(refreshBtn);

        // Add to Frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(tableScroll, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Action Listeners - No lambda expressions
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAddForm();
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showEditForm(recipeTable.getSelectedRow());
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.deleteRecipe(recipeTable.getSelectedRow());
            }
        });

        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.refreshRecipeList();
            }
        });

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.searchRecipes(searchField.getText());
            }
        });

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.searchRecipes(searchField.getText()); // Enter key
            }
        });

        sortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sortRecipes(ascBtn.isSelected());
            }
        });

        frame.setLocationRelativeTo(null);
    }

    // Update Table
    public void updateRecipeTable(List<Recipe> recipes) {
        tableModel.setRowCount(0);
        for (Recipe r : recipes) {
            tableModel.addRow(new Object[]{
                r.getTitle(),
                r.getCuisine(),
                r.getCookingTime(),
                r.getServings()
            });
        }
    }

    // Update Recent Panel (Carousel-like)
    public void updateRecentRecipesPanel(Queue<Recipe> recent) {
        recentPanel.removeAll();
        for (Recipe r : recent) {
            JPanel card = new JPanel();
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.add(new JLabel(r.getTitle()));
            card.add(new JLabel(r.getCuisine() + " • " + r.getCookingTime() + " mins"));
            recentPanel.add(card);
            recentPanel.add(Box.createHorizontalStrut(10));
        }
        recentPanel.revalidate();
        recentPanel.repaint();
    }

    // Update Stats
    public void updateStats(int total, int avgTime) {
        statsLabel.setText("Total Recipes: " + total + " | Avg Cooking Time: " + avgTime + " mins");
    }

    public JFrame getFrame() {
        return frame;
    }
}