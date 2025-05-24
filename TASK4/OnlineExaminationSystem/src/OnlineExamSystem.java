package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OnlineExamSystem extends JFrame implements ActionListener {
    JPanel mainPanel;
    CardLayout cardLayout;

    JPanel loginPanel, profilePanel, quizPanel, resultPanel;
    JTextField tfUsername = new JTextField(15);
    JPasswordField pfPassword = new JPasswordField(15);
    JTextField tfNewUsername = new JTextField(15);
    JPasswordField pfNewPassword = new JPasswordField(15);

    JLabel label = new JLabel();
    JRadioButton[] radioButton = new JRadioButton[4];
    ButtonGroup bg = new ButtonGroup();
    JButton btnNext = new JButton("Next");
    JButton btnSubmit = new JButton("Submit");
    JLabel timerLabel = new JLabel("Time left: 30");

    Timer timer;
    int timeLeft = 30;
    int score = 0, current = 0;
    String currentUser = "";

    List<Question> questions = new ArrayList<>();
    List<Integer> selectedAnswers = new ArrayList<>();

    Connection conn;

    // Blue Color Scheme
    Color DARK_BLUE = Color.decode("#0057C4");
    Color MEDIUM_BLUE = Color.decode("#008CC1");
    Color LIGHT_BLUE = Color.decode("#5BD5FF");
    Color VERY_LIGHT_BLUE = Color.decode("#B4ECFF");
    Color ALMOST_WHITE = Color.decode("#EFFDFF");

    public OnlineExamSystem() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Online Examination System");

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initDB();
        initLoginPanel();
        initProfilePanel();
        initQuizPanel();
        initResultPanel();

        add(mainPanel);
        setVisible(true);
    }

    void initDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/OnlineExamDB", "root", "1234abc");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }

    void initLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(DARK_BLUE);

        JPanel content = new JPanel(new GridLayout(0, 1, 10, 10));
        content.setBackground(DARK_BLUE);

        JLabel title = new JLabel("Welcome to Online Exam System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(ALMOST_WHITE);

        content.add(title);
        content.add(createLabel("Username:"));
        content.add(tfUsername);
        content.add(createLabel("Password:"));
        content.add(pfPassword);

        JButton btnLogin = createButton("Login");
        JButton btnRegister = createButton("Register");

        content.add(btnLogin);
        content.add(btnRegister);

        loginPanel.add(content);
        mainPanel.add(loginPanel, "login");
    }

    void initProfilePanel() {
        profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(MEDIUM_BLUE);

        JPanel content = new JPanel(new GridLayout(0, 1, 10, 10));
        content.setBackground(MEDIUM_BLUE);

        JLabel title = new JLabel("Update Profile", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(ALMOST_WHITE);

        content.add(title);
        content.add(createLabel("New Username:"));
        content.add(tfNewUsername);
        content.add(createLabel("New Password:"));
        content.add(pfNewPassword);

        JButton btnUpdate = createButton("Update");
        JButton btnStartExam = createButton("Start Exam");
        JButton btnLogout = createButton("Logout");

        content.add(btnUpdate);
        content.add(btnStartExam);
        content.add(btnLogout);

        profilePanel.add(content);
        mainPanel.add(profilePanel, "profile");
    }

    void initQuizPanel() {
        quizPanel = new JPanel(null);
        quizPanel.setBackground(DARK_BLUE);

        label.setBounds(30, 30, 1000, 30);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(ALMOST_WHITE);
        quizPanel.add(label);

        timerLabel.setBounds(1000, 10, 200, 25);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timerLabel.setForeground(ALMOST_WHITE);
        quizPanel.add(timerLabel);

        for (int i = 0; i < 4; i++) {
            radioButton[i] = new JRadioButton();
            radioButton[i].setBounds(50, 80 + i * 40, 900, 25);
            radioButton[i].setBackground(VERY_LIGHT_BLUE);
            radioButton[i].setForeground(Color.BLACK);
            bg.add(radioButton[i]);
            quizPanel.add(radioButton[i]);
        }

        btnNext.setBounds(300, 300, 100, 30);
        btnSubmit.setBounds(420, 300, 100, 30);

        customizeButton(btnNext);
        customizeButton(btnSubmit);

        btnNext.addActionListener(this);
        btnSubmit.addActionListener(this);

        quizPanel.add(btnNext);
        quizPanel.add(btnSubmit);

        mainPanel.add(quizPanel, "quiz");
    }

    void initResultPanel() {
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(ALMOST_WHITE);

        JLabel title = new JLabel("Performance Analysis", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(DARK_BLUE);

        resultPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(resultPanel, "result");
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            switch (cmd) {
                case "Login" -> login();
                case "Register" -> register();
                case "Update" -> updateProfile();
                case "Start Exam" -> startExam();
                case "Next" -> nextQuestion();
                case "Submit" -> submitExam();
                case "Logout" -> logout();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    void login() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
        ps.setString(1, tfUsername.getText().trim());
        ps.setString(2, new String(pfPassword.getPassword()).trim());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            currentUser = tfUsername.getText().trim();
            cardLayout.show(mainPanel, "profile");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
        }
    }

    void register() throws SQLException {
        PreparedStatement check = conn.prepareStatement("SELECT * FROM users WHERE username=?");
        check.setString(1, tfUsername.getText().trim());
        ResultSet rs = check.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Username already exists!");
        } else {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            ps.setString(1, tfUsername.getText().trim());
            ps.setString(2, new String(pfPassword.getPassword()).trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registered successfully!");
        }
    }

    void updateProfile() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE users SET username=?, password=? WHERE username=?");
        ps.setString(1, tfNewUsername.getText().trim());
        ps.setString(2, new String(pfNewPassword.getPassword()).trim());
        ps.setString(3, currentUser);
        ps.executeUpdate();
        currentUser = tfNewUsername.getText().trim();
        JOptionPane.showMessageDialog(this, "Profile updated!");
    }

    void startExam() {
        score = 0;
        current = 0;
        selectedAnswers.clear();
        loadQuestions();
        setQuestion();
        btnNext.setEnabled(true);
        cardLayout.show(mainPanel, "quiz");
        startTimer();
    }

    void nextQuestion() {
        saveAnswer();
        current++;
        if (current < questions.size()) {
            setQuestion();
            resetTimer();
        } else {
            btnNext.setEnabled(false);
            if (timer != null)
                timer.cancel();
            showResults();
            cardLayout.show(mainPanel, "result");
        }
    }

    void submitExam() {
        saveAnswer();
        if (timer != null)
            timer.cancel();
        showResults();
        cardLayout.show(mainPanel, "result");
    }

    void logout() {
        if (timer != null)
            timer.cancel();
        tfUsername.setText("");
        pfPassword.setText("");
        tfNewUsername.setText("");
        pfNewPassword.setText("");
        currentUser = "";
        cardLayout.show(mainPanel, "login");
    }

    void saveAnswer() {
        for (int i = 0; i < 4; i++) {
            if (radioButton[i].isSelected()) {
                selectedAnswers.add(i + 1);
                if (i + 1 == questions.get(current).correctOption) {
                    score++;
                }
                return;
            }
        }
        selectedAnswers.add(0); // not answered
    }

    void setQuestion() {
        bg.clearSelection();
        label.setText("Q" + (current + 1) + ": " + questions.get(current).question);
        radioButton[0].setText(questions.get(current).option1);
        radioButton[1].setText(questions.get(current).option2);
        radioButton[2].setText(questions.get(current).option3);
        radioButton[3].setText(questions.get(current).option4);
    }

    void loadQuestions() {
        questions.clear();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM questions LIMIT 10");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                questions.add(new Question(
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        rs.getInt("correct_option")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading questions: " + e.getMessage());
        }
    }

    void startTimer() {
        timeLeft = 30;
        timerLabel.setText("Time left: " + timeLeft);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timeLeft--;
                    timerLabel.setText("Time left: " + timeLeft);
                    if (timeLeft <= 0) {
                        nextQuestion();
                    }
                });
            }
        }, 1000, 1000);
    }

    void resetTimer() {
        if (timer != null)
            timer.cancel();
        startTimer();
    }

    void showResults() {
        JPanel content = new JPanel(new GridLayout(0, 1));
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int selected = (i < selectedAnswers.size()) ? selectedAnswers.get(i) : 0;
            boolean correct = selected == q.correctOption;
            JLabel resultLabel = new JLabel(
                    "<html><b>Q" + (i + 1) + ":</b> " + q.question +
                            "<br>Selected: " + (selected > 0 ? q.getOptionText(selected) : "Not Answered") +
                            "<br>Correct: " + q.getOptionText(q.correctOption) +
                            "<br><font color='" + (correct ? "green" : "red") + "'>" + (correct ? "Correct" : "Wrong")
                            + "</font><br><br></html>");
            content.add(resultLabel);
        }

        JScrollPane scrollPane = new JScrollPane(content);
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(this, "Your Score: " + score + "/" + questions.size());
    }

    JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(ALMOST_WHITE);
        return label;
    }

    JButton createButton(String text) {
        JButton button = new JButton(text);
        customizeButton(button);
        button.addActionListener(this);
        return button;
    }

    void customizeButton(JButton button) {
        button.setBackground(LIGHT_BLUE);
        button.setForeground(Color.BLACK);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OnlineExamSystem::new);
    }

    class Question {
        String question, option1, option2, option3, option4;
        int correctOption;

        public Question(String question, String o1, String o2, String o3, String o4, int correct) {
            this.question = question;
            this.option1 = o1;
            this.option2 = o2;
            this.option3 = o3;
            this.option4 = o4;
            this.correctOption = correct;
        }

        String getOptionText(int optionNumber) {
            return switch (optionNumber) {
                case 1 -> option1;
                case 2 -> option2;
                case 3 -> option3;
                case 4 -> option4;
                default -> "Not answered";
            };
        }
    }
}