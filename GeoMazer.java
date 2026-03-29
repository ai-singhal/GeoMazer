// Aryan Singhal
// 5/18/22 (Due: 5/19/22)
// Period 1
// GeoMazer.java
// GeoMazer is a game about learning Geography. This game is aimed towards
// both middle and high school students with questions for beginners and advanced.
// Players will learn about the geography of the USA and/or the World in a fun and
// interactive way as they answer questions to reach the treasure.
/// Week 5: Program functioning and easy to play. Last-minute refinement
/// to the program structure and comments.

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.ComponentOrientation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Insets;

public class GeoMazer
{
    // Main CardPanel to cards for the GeoMazer game pages
    private CardPanel cp;
    private CardLayout cards;

    // Fonts
    private Font font1;
    private Font font2;
    private Font font3;
    private Font font4;
    private Font font5;

    // Colors
    private Color BROWN;
    private Color GOLD;
    private Color DARKYELLOW;
    private Color LIGHTBLUE;
    private Color LIGHTGREEN;
    private Color LIGHTRED;
    private Color LIGHTYELLOW;
    private Color LIGHTGRAY;
    private Color VERYLIGHTYELLOW;
    private Color VERYLIGHTRED;
    private Color VERYLIGHTBLUE;
    private Color VERYLIGHTGREEN;

    // Game pictures
    private Image gameIcon;
    private Image stars;
    private Image globe;

    // for storing user's name from Game Settings. Used during gameplay.
    private String playerName; // for the player's name in the game play
    private boolean isUSA, isWorld; // helps determine what set of questions to choose from
    private boolean isBeginner, isAdvanced; // chose learning difficulty

    private JLabel playerWelcomeLabel; // for the player's label at top in the game play
    private JTextArea playerStatsText; // for the player's stats text information in the game play
    private JButton lifeButton; // Button to get a Lifeline when there is no way out.
    private JButton rankingButton; // Button to show the player's rankings in Leaderboard

    // For managing if instruction window is opened/closed
    private boolean isInstructionWindowOpen;
    private JFrame iFrame; // instruction frame shared by cards 1 and 3

    private Timer timer; // Timer
    private int elapsedTime; // time since start
    private String playerTimeText; // the player's time in HH:MM:SS

    private JLabel leaderLabel; // Label at top for the Leaderboard page, either for Beginner and Advanced.

    // For the player stats that are going to be written to the Leaderboard text file
    private String writeStatsText;

    private JTextArea rankingArea; // Leaderboard ranking

    // initializes all private variables above.
    public GeoMazer() 
    {
        font1 = new Font("Tahoma", Font.BOLD, 40);
        font2 = new Font("Tahoma", Font.PLAIN, 25);
        font3 = new Font("Tahoma", Font.PLAIN, 16);
        font4 = new Font("Tahoma", Font.BOLD, 18);
        font5 = new Font("Tahoma", Font.BOLD, 24);

        BROWN = new Color(150, 51, 0);
        DARKYELLOW = new Color(255, 204, 0);
        GOLD = new Color(255, 204, 51);
        LIGHTBLUE = new Color(51, 153, 255);
        LIGHTGREEN = new Color(102, 255, 102);
        LIGHTRED = new Color(255, 102, 102);
        LIGHTYELLOW = new Color(255, 255, 153);
        LIGHTGRAY = new Color(204, 204, 204);
        VERYLIGHTYELLOW = new Color(255, 255, 204);
        VERYLIGHTRED = new Color(255, 150, 150);
        VERYLIGHTBLUE = new Color(173, 216, 230);
        VERYLIGHTGREEN = new Color(150, 255, 200);

        elapsedTime = 0;
        timer = new Timer(1000, new TimerHandler());

        playerTimeText = "    [00:00:00]";
        isInstructionWindowOpen = false;
    }

    public static void main(String[] args)
    {
        GeoMazer gm = new GeoMazer();
        gm.run();
    }

    // Game frame with card layout
    public void run()
    {
        JFrame frame = new JFrame("GeoMazer");
        frame.setSize(810, 730);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        cp = new CardPanel();
        frame.add(cp);

        frame.setVisible(true);
    }

    public class CardPanel extends JPanel
    {
        // For all the pages for the game
        public CardPanel()
        {
            cards = new CardLayout();
            setLayout(cards);

            StartPanel sp = new StartPanel();
            GameSettings gs = new GameSettings();
            GamePlay gp = new GamePlay();
            LeaderBoard lb = new LeaderBoard();
            PurposePanel pur = new PurposePanel();
            Credits cd = new Credits();

            add(sp, "Home Page");
            add(gs, "Game Settings");
            add(gp, "Play Page");
            add(lb, "LeaderBoard");
            add(pur, "Purpose");
            add(cd, "Credits");
        }
    }

    public class StartPanel extends JPanel
    {
        // components in start panel. button navigation.
        public StartPanel()
        {
            setLayout(null);

            stars = getMyImage("./pictures/" + "stars.jpg");
            globe = getMyImage("./pictures/" + "globe.png");
            gameIcon = getMyImage("./pictures/" + "icon.png");

            JLabel geoMazerLabel = new JLabel("GEOMAZER");
            geoMazerLabel.setBounds(270, 20, 350, 50);
            geoMazerLabel.setFont(font1);
            geoMazerLabel.setForeground(Color.WHITE);
            add(geoMazerLabel);

            JLabel slogan = new JLabel("\"The World's BEST Educational Geography Game!\"");
            slogan.setBounds(120, 80, 600, 30);
            slogan.setFont(new Font("Tahoma", Font.ITALIC, 22));
            slogan.setForeground(Color.WHITE);
            add(slogan);

            JButton purButton = createButton("Why Play GeoMazer?", LIGHTRED, VERYLIGHTRED, font3);
            purposeButtonHandler purbh = new purposeButtonHandler();
            purButton.addActionListener(purbh);
            purButton.setBounds(80, 170, 200, 100);
            purButton.setBorder(BorderFactory.createLineBorder(VERYLIGHTRED, 10));
            add(purButton);

            JButton iButton = createButton("How to Play?", DARKYELLOW, LIGHTYELLOW, font2);
            instructionButtonHandler ibh = new instructionButtonHandler();
            iButton.addActionListener(ibh);
            iButton.setBounds(80, 300, 200, 150);
            iButton.setBorder(BorderFactory.createLineBorder(LIGHTYELLOW, 10));
            add(iButton);

            JButton pButton = createButton("Play!", Color.GREEN, LIGHTGREEN, font1);
            playButtonHandler pbh = new playButtonHandler();
            pButton.addActionListener(pbh);
            pButton.setBounds(350, 200, 300, 150);
            pButton.setBorder(BorderFactory.createLineBorder(LIGHTGREEN, 10));
            add(pButton);

            JButton creditsButton = createButton("Credits", LIGHTBLUE, VERYLIGHTBLUE, font3);
            creditsButtonHandler crbh = new creditsButtonHandler();
            creditsButton.addActionListener(crbh);
            creditsButton.setBounds(30, 630, 100, 50);
            add(creditsButton);

            JButton closeButton = createButton("X", Color.RED, LIGHTRED, font2);
            closeButtonHandler cbh = new closeButtonHandler();
            closeButton.addActionListener(cbh);
            closeButton.setBounds(10, 10, 60, 60);
            add(closeButton);
        }

        // This will take the player to the Game Settings page.
        public class purposeButtonHandler implements ActionListener
		{
            public void actionPerformed(ActionEvent evt)
            {
                cards.show(cp, "Purpose");
            }
        }

        // This will take the player to the Game Settings page.
        public class playButtonHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                timer.stop();
                cards.show(cp, "Game Settings");
            }
        }

        // This will take the player to the Credits page
        public class creditsButtonHandler implements ActionListener
		{
            public void actionPerformed(ActionEvent evt)
            {
                cards.show(cp, "Credits");
            }
        }

        public class closeButtonHandler implements ActionListener
        {
            // closes program
            public void actionPerformed(ActionEvent evt)
            {
                System.exit(1);
            }
        }

        // Paint Component to draw Images.
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            ImageIcon starIcon = new ImageIcon(stars.getScaledInstance(this.getWidth(),
                this.getHeight(), Image.SCALE_DEFAULT));
            starIcon.paintIcon(this, g, 0, 0);

            g.drawImage(globe, 270, 320, 950, 900, this);

            int panelWidth = this.getBounds().width;
            g.drawImage(gameIcon, panelWidth - 100, 10, 80, 100, this);

        }
    } // end of StartPanel

    public class GameSettings extends JPanel
    {
        private JRadioButton chooseUSA, chooseWorld; // choose learning area
        // Checks if the user has entered in values for each component in game settings
        private boolean isNamePicked, isDifficultyPicked, isLearningAreaPicked;
        private JButton playButton, homeButton; // go back to start panel or play game

        // Panels laid out for settings page. In a border layout
        public GameSettings()
        {
            isBeginner = false; // Difficulty level beginner
            isAdvanced = false; // Difficulty level advanced

            isUSA = false; // radio button selection for questions from USA
            isWorld = false; // radio button selection for questions from World

            isNamePicked = false;
            isDifficultyPicked = false;
            isLearningAreaPicked = false;

            setLayout(new BorderLayout());

            JLabel settingsLabel = new JLabel("Game Settings", SwingConstants.CENTER);
            settingsLabel.setForeground(Color.BLACK);
            settingsLabel.setFont(font5);
            settingsLabel.setPreferredSize(new Dimension(500, 70));
            settingsLabel.setBackground(GOLD);
            settingsLabel.setOpaque(true);
            add(settingsLabel, BorderLayout.NORTH);

            JPanel setCp = makeSettingsCenterPanel();
            add(setCp, BorderLayout.CENTER);

            JPanel setSp = makeSettingsBottomPanel();
            add(setSp, BorderLayout.SOUTH);

            playButton.setEnabled(false);
        }

        // Adds all the panels to the center of the settings page panel. Border Layout
        public JPanel makeSettingsCenterPanel()
        {
            JPanel settings = new JPanel();
            settings.setLayout(new BorderLayout());

            JPanel levelPanel = makeNameLevelPanel();
            settings.add(levelPanel, BorderLayout.CENTER);

            JPanel learningArea = makeLearningAreaPanel();
            settings.add(learningArea, BorderLayout.SOUTH);

            return settings;
        }

        // Text field for user to enter in name. Menu bar to select difficulty of questions.
        // Both components in a flow layout.
        public JPanel makeNameLevelPanel()
        {
            JPanel levelPanel = new JPanel();
            levelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 300, 100));
            levelPanel.setBackground(VERYLIGHTYELLOW);

            JTextField tfName = new JTextField("Enter Your First Name");
            tfName.setPreferredSize(new Dimension(400, 50));
            tfName.setFont(font3);
            nameFieldHandler nfh = new nameFieldHandler();
            tfName.addActionListener(nfh);
            levelPanel.add(tfName);

            DifficultyMenuBar dMB = new DifficultyMenuBar();
            levelPanel.add(dMB.getMenuBar());
            return levelPanel;
        }

        // Makes the menu bar for question difficulty.
        public class DifficultyMenuBar implements ActionListener
        {
            private JMenuItem blevel, alevel; // beginner and advanced levels
            private JMenu menu; // Jmenu
            private JMenuBar menubar; // menubar

            // Both choices in menu bar are originally light yellow
            public DifficultyMenuBar()
            {
                menubar = new JMenuBar();
                menu = new JMenu("Choose Difficulty", true);

                blevel = new JMenuItem("Beginner");
                alevel = new JMenuItem("Advanced");
                blevel.setBackground(LIGHTYELLOW);
                alevel.setBackground(LIGHTYELLOW);

                blevel.addActionListener(this);
                alevel.addActionListener(this);
                menu.add(blevel);
                menu.add(alevel);
                menu.setFont(font4);
                menubar.add(menu);
            }

            public JMenuBar getMenuBar()
            {
                return menubar;
            }

            // If either beginner or advanced menu item is selected by user, the text on the
            // menu bar changes to clarify what was selected and the selected menu choice
            // changes from light yellow to dark yellow.
            public void actionPerformed(ActionEvent evt)
            {
                isBeginner = false;
                isAdvanced = false;

                String command = evt.getActionCommand();
                if(command.equals("Beginner"))
                {
                    blevel.setBackground(DARKYELLOW);
                    alevel.setBackground(LIGHTYELLOW);

                    menu.setText("Difficulty: Beginner");
                    menu.setForeground(BROWN);
                    menu.setOpaque(true);

                    isBeginner = true;
                    isAdvanced = false;
                    isDifficultyPicked = true;
                    leaderLabel.setText("Leaderboard Beginner Players");
                }
                else if(command.equals("Advanced"))
                {
                    alevel.setBackground(DARKYELLOW);
                    blevel.setBackground(LIGHTYELLOW);

                    menu.setText("Difficulty: Advanced");
                    menu.setForeground(BROWN);
                    menu.setOpaque(true);

                    isAdvanced = true;
                    isBeginner = false;
                    isDifficultyPicked = true;
                    leaderLabel.setText("Leaderboard Advanced Players");
                }
                else
                    menu.setText("Choose Difficulty");
				
				// Allow playing only when all the settings are available.
                if(isLearningAreaPicked && isNamePicked && isDifficultyPicked)
                    playButton.setEnabled(true);
            }
        }

        // Panel to make radio buttons for user to select question learning area.
        public JPanel makeLearningAreaPanel()
        {
            JPanel learnPanel = new JPanel();
            learnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 40));

            learnPanel.setPreferredSize(new Dimension(500, 100));

            JLabel chooseLabel = new JLabel("Choose Learning Area", SwingConstants.LEFT);
            chooseLabel.setForeground(Color.BLACK);
            chooseLabel.setFont(font4);
            chooseLabel.setBackground(VERYLIGHTBLUE);
            chooseLabel.setOpaque(true);
            learnPanel.add(chooseLabel);

            chooseUSA = new JRadioButton("USA");
            chooseWorld = new JRadioButton("World");

            learningAreaButtonsHandler labh = new learningAreaButtonsHandler();
            chooseUSA.addActionListener(labh);
            chooseWorld.addActionListener(labh);

            learnPanel.add(chooseUSA);
            learnPanel.add(chooseWorld);

            learnPanel.setBackground(VERYLIGHTBLUE);
            chooseUSA.setBackground(VERYLIGHTBLUE);
            chooseWorld.setBackground(VERYLIGHTBLUE);
            chooseUSA.setFont(font3);
            chooseWorld.setFont(font3);

            return learnPanel;
        }

        // Bottom panel of the game settings page allows user to navigate.
        public JPanel makeSettingsBottomPanel()
        {
            JPanel bPanel = new JPanel();
            bPanel.setLayout(new BorderLayout());

            JLabel blabel = new JLabel("Please complete the game settings. " +
                "Press Play to proceed.", SwingConstants.CENTER);
            blabel.setForeground(Color.BLACK);
            blabel.setFont(new Font("Tahoma", Font.BOLD, 16));
            blabel.setPreferredSize(new Dimension(500, 100));
            blabel.setBackground(GOLD);
            blabel.setOpaque(true);
            bPanel.add(blabel, BorderLayout.CENTER);

            playButton = new JButton("Play Game!");
            playButtonHandler playbh = new playButtonHandler();
            playButton.addActionListener(playbh);
            playButton.setFont(font3);
            playButton.setBackground(LIGHTGREEN);
            playButton.setOpaque(true);
            playButton.setBorderPainted(false);
            bPanel.add(playButton, BorderLayout.EAST);

            JButton homeButton = new JButton("Back Home!");
            homeButtonHandler homebh = new homeButtonHandler();
            homeButton.addActionListener(homebh);
            homeButton.setFont(font3);
            homeButton.setBackground(LIGHTRED);
            homeButton.setOpaque(true);
            homeButton.setBorderPainted(false);
            bPanel.add(homeButton, BorderLayout.WEST);

            return bPanel;
        }

        // Gets user's name and uses it to write their name on the Game play page
        public class nameFieldHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                playerName = evt.getActionCommand();
                isNamePicked = true;
                playerWelcomeLabel.setText(playerName + "'s GeoMazer" + "  [00:00:00]");
                playerStatsText.setText(" " + playerName + "'s Stats");

                if(isLearningAreaPicked && isNamePicked && isDifficultyPicked)
                    playButton.setEnabled(true);
            }
        }

        public class playButtonHandler implements ActionListener
        {
            // goes to play game page
            public void actionPerformed(ActionEvent evt)
            {
                cards.show(cp, "Play Page");
            }
        }

        // Not in the same button group. if USA is selected, questions from USA. if World is
        // selected, questions from World. If both are selected, both are true and random
        // questions from both USA and World.
        public class learningAreaButtonsHandler implements ActionListener
        {
            // determine learning area
            public void actionPerformed(ActionEvent evt)
            {
                if(chooseUSA.isSelected())
                {
                    isUSA = true;
                    isLearningAreaPicked = true;
                }
                
                if(chooseWorld.isSelected())
                {
                    isWorld = true;
                    isLearningAreaPicked = true;
                }

                if(isLearningAreaPicked && isNamePicked && isDifficultyPicked)
                    playButton.setEnabled(true);
            }
        }
    } // end of GameSettings


    // GamePlay panel for user to play the game
    public class GamePlay extends JPanel
    {
        private final int ROW, COLUMN; // Grid dimensions with cells
        // Hold cell states (0, 1, 2, 3) 0: Not opened 1: opened not answered
        // 2: Answered correctly 3: Answered wrong
        private int[][] cellState; // the current cell on the grid
        private int currentCellRow; // current cell row that user has chosen
        private int currentCellColumn; // current cell column user has chosen
        private JPanel gPanel; // grid panel holding array of JButton cells

		// images used for the cells
		private Image brickImage;
		private Image startImage;
		private Image pathImage;
		private Image treasureCloseImage;
		private Image treasureImage;
		
        // Card layout for the question panel. Each question is a card.
        private CardLayout qCards;
        private QuestionCardPanel qcp; // holding question cards
        // whether question cards are already added to the question panel
        private boolean areQuestionsAdded; 

        // Bank of questions read from files and randomized.
        QuestionBank easyUSABank;
        QuestionBank hardUSABank;
        QuestionBank easyWorldBank;
        QuestionBank hardWorldBank;
        QuestionBank easyBothBank;
        QuestionBank hardBothBank;

        private int qBankSize; // # of questions in the question bank
        // the indices of the questions that have already been shown
        private int[] questionShownIndices;
        // the number of questions that have been shown from the question bank
        private int numQuestionsShown;

        private boolean isQuestionOpen; // Allow only one open cell (question)
        // for opening the feedback frame after user clicks submit. 
        private boolean isFeedbackFrameOpen;
        
		// is the timer already running.
        boolean timerStarted;

        private JButton homeButton; // Button to go home and restart the game

        private playerStats pStats; // for creating stats for the player
        private boolean gameOver; // is the game over or not

        // GamePlay constructor. Prepares the Game panel with border layout 
        // Top panel containing "How to Play" button, player name, timer, and game icon
        // Center panel for play area with grid cells and question panel for question cards
        // Bottom panel for home navigation and stats.
        public GamePlay()
        {
            setLayout(new BorderLayout());

            ROW = 6;
            COLUMN = 6;

            qBankSize = 0;
            questionShownIndices = new int[50];
            numQuestionsShown = 0;

            areQuestionsAdded = false;
            isFeedbackFrameOpen = false; // whether feedback frame is open
            isQuestionOpen = false; // whether user is working on a question
             // Hold cell states (0, 1, 2, 3)0: Not opened 1: opened not answered
             // 2: Answered correctly 3: Answered wrong
            cellState = new int[ROW][COLUMN];
            currentCellRow = -1; // current cell row
            currentCellColumn = -1; // current cell column
            for(int i = 0; i < ROW; i++)
            {
                for(int j = 0; j < COLUMN; j++)
                {
                    cellState[i][j] = 0;
                }
            }
            
            brickImage = new ImageIcon("pictures/brick.png").getImage();
			startImage = new ImageIcon("pictures/start.png").getImage();
			pathImage = new ImageIcon("pictures/path.png").getImage();
			treasureCloseImage = new ImageIcon("pictures/treasureClose.png").getImage();
			treasureImage = new ImageIcon("pictures/treasure.png").getImage();
			
            pStats = new playerStats();

            elapsedTime = 0;
            playerTimeText = "    [00:00:00]";
            timerStarted = false;
            gameOver = false;

            easyUSABank = new QuestionBank("questions/usa_easy_questions.txt");
            easyUSABank.readQuestion();

            hardUSABank = new QuestionBank("questions/usa_hard_questions.txt");
            hardUSABank.readQuestion();

            easyWorldBank = new QuestionBank("questions/world_easy_questions.txt");
            easyWorldBank.readQuestion();

            hardWorldBank = new QuestionBank("questions/world_hard_questions.txt");
            hardWorldBank.readQuestion();

            easyBothBank = new QuestionBank("questions/both_easy_questions.txt");
            easyBothBank.readQuestion();

            hardBothBank = new QuestionBank("questions/both_hard_questions.txt");
            hardBothBank.readQuestion();

            // Top Panel
            JPanel topP = makePlayTopPanel();
            add(topP, BorderLayout.NORTH);

            // Main Play area with GridCellPanel and QuestionPanel
            JPanel centerP = makePlayCenterPanel();
            add(centerP, BorderLayout.CENTER);

            // Navigation and Stats
            JPanel bottomP = makePlayBottomPanel();
            add(bottomP, BorderLayout.SOUTH);
        }

        // Adds components to the top of the GamePlay border layout. Panel is another
        // border layout. Creates new instance of timer.
        public JPanel makePlayTopPanel()
        {
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout());

            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BorderLayout());

            // Label with playerTimeText powered by Timer
            playerWelcomeLabel = new JLabel(playerName + "'s GeoMazer" + playerTimeText,
                SwingConstants.CENTER);
            playerWelcomeLabel.setForeground(Color.BLACK);
            playerWelcomeLabel.setFont(font4);
            playerWelcomeLabel.setPreferredSize(new Dimension(500, 50));
            playerWelcomeLabel.setBackground(GOLD);
            playerWelcomeLabel.setOpaque(true);

            centerPanel.add(playerWelcomeLabel, BorderLayout.CENTER);
            topPanel.add(centerPanel, BorderLayout.CENTER);

            // How to Play button to pop up Instruction JFrame window
            JButton howButton = createButton("Instructions", LIGHTRED, LIGHTRED, font3);
            instructionButtonHandler ibh = new instructionButtonHandler();
            howButton.addActionListener(ibh);
            topPanel.add(howButton, BorderLayout.WEST);

            // Game Icon
            ImageIcon gameplayIcon = new ImageIcon(gameIcon.getScaledInstance(40,
                50, Image.SCALE_DEFAULT));
            JButton gameplayIconButton = new JButton(gameplayIcon);
            gameplayIconButton.setBackground(GOLD);
            gameplayIconButton.setOpaque(true);
            gameplayIconButton.setBorderPainted(false);
            topPanel.add(gameplayIconButton, BorderLayout.EAST);

            // Fires every second

            return topPanel;
        }

        // Main PlayCenter panel comprising
        // - GridPanel with ROWxCOLUMN Jbuttons cell that user click to seek treasure
        // - QuestionCardPanel to hold question cards
        public JPanel makePlayCenterPanel()
        {
            JPanel playCenterPanel = new JPanel();
            playCenterPanel.setLayout(new BorderLayout());

            // Panel for clicking the cell to get to the treasure
            playCenterPanel.add(makeGridPanel(), BorderLayout.CENTER);

            // Panel for question, answer choices, and submit
            qcp = new QuestionCardPanel();
            playCenterPanel.add(qcp, BorderLayout.EAST);

            return playCenterPanel;
        }

        // Makes ROW X COLUMN grid layout with JButton cells.
        public JPanel makeGridPanel()
        {
            gPanel = new JPanel();
            gPanel.setLayout(new GridLayout(ROW, COLUMN, 1, 1));

            for(int i = 0; i < ROW; i++)
            {
                for(int j = 0; j < COLUMN; j++)
                {
                    gPanel.add(makeGridCell(i, j));
                }
            }
            return gPanel;
        }

        // Make each cell JButton giving it a name
        // i, j input params are the coordinates of the cell, with top left as (0, 0)
        public JButton makeGridCell(int i, int j)
        {			
            String cellText = "?";
            JButton cButton = new JButton(cellText);
            cButton.setFont(font3);
            cellButtonHandler cbh = new cellButtonHandler();
            cButton.addActionListener(cbh);
            cellButtonMouseListner cml = new cellButtonMouseListner();
            cButton.addMouseListener(cml);

            cButton.setBackground(LIGHTYELLOW);
            cButton.setForeground(BROWN);
            cButton.setName(String.valueOf(i) + "," + String.valueOf(j));
            cButton.setOpaque(true);
            cButton.setBorderPainted(true);
            cButton.setBorder(BorderFactory.createLineBorder(LIGHTBLUE, 5));

            if(i == ROW - 1 && j == 0) // Start cell
            {
                cellState[i][j] = 2; // origin is already marked as green
                cButton.setBackground(LIGHTGREEN);
                cButton.setHorizontalAlignment(SwingConstants.LEFT);
                cButton.setIcon(new ImageIcon(startImage.getScaledInstance(70, 75,
					java.awt.Image.SCALE_SMOOTH)));
            }
            if(i == 0 && j == COLUMN - 1) // Treasure cell
            {
                cButton.setHorizontalAlignment(SwingConstants.LEFT);
                cButton.setIcon(new ImageIcon(treasureCloseImage.getScaledInstance
					(70, 75, java.awt.Image.SCALE_SMOOTH)));
			}

            return cButton;
        }

        // Determines if a cell at position(i, j) can be clicked
        public boolean isCellClickable(int i, int j)
        {
            if(cellState[i][j] > 0) // already clicked
                return false;

            boolean isLeftGreen = false, isRightGreen = false, isTopGreen = false,
                isBottomGreen = false;

            if(i - 1 >= 0 && cellState[i - 1][j] == 2)
                isLeftGreen = true;
            if(i + 1 < ROW && cellState[i + 1][j] == 2)
                isRightGreen = true;
            if(j - 1 >= 0 && cellState[i][j - 1] == 2)
                isTopGreen = true;
            if(j + 1 < COLUMN && cellState[i][j + 1] == 2)
                isBottomGreen = true;

			// Clickable if there is a green neighbor cell on either left, right, top, or bottom.
            if(isLeftGreen || isRightGreen || isTopGreen || isBottomGreen)
                return true;

            return false;
        }

        // Handler for the grid cell buttons
        public class cellButtonHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                JButton cellButton = (JButton)(evt.getSource());
                String[] arr = cellButton.getName().split(",");

                // Gets the coordinates of the cell from its component name.
                int i = Integer.parseInt(arr[0]);
                int j = Integer.parseInt(arr[1]);

                if(!areQuestionsAdded)
                {
                    qcp.addQuestions();
                    areQuestionsAdded = true;
                }

                if(isCellClickable(i, j) && !isFeedbackFrameOpen &&
                    !isQuestionOpen && !gameOver)
                {
                    cellState[i][j] = 1;  // clicked but not yet answered
                    currentCellRow = i;
                    currentCellColumn = j;

                    if(i == 0 && j == COLUMN - 1) // Reached Treasure cell
                    {
                        timer.stop();
                        writeStatsToFile();
                        qCards.show(qcp, "Win"); // show the win card in question panel

						// Replace closed treasure with opened treasure image on the treasure cell.
					    cellButton.setHorizontalAlignment(SwingConstants.LEFT);
                        int width = cellButton.getWidth();
						int height = cellButton.getHeight();
						cellButton.setIcon(null);
						cellButton.setIcon(new ImageIcon(treasureImage.getScaledInstance(width,
							height, java.awt.Image.SCALE_SMOOTH)));

                        gameOver = true;
                        lifeButton.setEnabled(false);
                        isQuestionOpen = false;
                        rankingButton.setEnabled(true);
                    }
                    else
                    {
                        if(!timerStarted)
                        {
                            timer.restart();
                            timerStarted = true;
                        }

                        cellButton.setBackground(VERYLIGHTBLUE);
                        cellButton.setText("Open");

                        // Pick a random question for the cell to show in the QuestionPanel
                        int k = getRadioQuestionIndex(qBankSize);
                        String cardToShow = "Q" + Integer.toString(k);
                        qCards.show(qcp, cardToShow);
                        isQuestionOpen = true;
                    }
                }
            }
        }

        // When the cell is clicked, gets the position of the cell and calls the 
        // highLightNeighbors method to find the path possibilities.
        public class cellButtonMouseListner implements MouseListener
        {
            public void mousePressed(MouseEvent evt)
            {
                JButton cellButtonPressed = (JButton)(evt.getSource());
                String[] arr = cellButtonPressed.getName().split(",");

                // Gets the coordinates of the cell from its component name.
                int i = Integer.parseInt(arr[0]);
                int j = Integer.parseInt(arr[1]);

                highLightNeighbors(i, j, VERYLIGHTGREEN);
            }

            public void mouseEntered(MouseEvent evt) {}
            public void mouseExited(MouseEvent evt) {}
            public void mouseClicked(MouseEvent evt) {}

            public void mouseReleased(MouseEvent evt)
            {
                JButton cellButtonReleased = (JButton)(evt.getSource());
                String[] arr = cellButtonReleased.getName().split(",");

                // Gets the coordinates of the cell from its component name.
                int i = Integer.parseInt(arr[0]);
                int j = Integer.parseInt(arr[1]);

                highLightNeighbors(i, j, LIGHTYELLOW);
            }
        }

        // When clicked on a green cell, highlight potential clickable cells for 
        // path building.
        public void highLightNeighbors(int i, int j, Color c)
        {
            if(cellState[i][j] != 2)
                return; // No hightlighting

            if(i - 1 >= 0 && cellState[i - 1][j] == 0)
                getButtonXAndY(gPanel, i - 1, j).setBackground(c);
            if(i + 1 < ROW && cellState[i + 1][j] == 0)
                getButtonXAndY(gPanel, i + 1, j).setBackground(c);
            if(j - 1 >= 0 && cellState[i][j - 1] == 0)
                getButtonXAndY(gPanel, i, j - 1).setBackground(c);
            if(j + 1 < COLUMN && cellState[i][j + 1] == 0)
                getButtonXAndY(gPanel, i, j + 1).setBackground(c);

            return;
        }

        // Gets the JButton component in GridLayout given (i, j) coordinates
        // Used to update the cell backgroundcolor when answer is right/wrong
        public JButton getButtonXAndY(JPanel p, int x, int y)
        {
            return (JButton) p.getComponent((x * ROW) + y);
        }

        // CardLayout panel to show Questions as cards
        // Each question is a panel comprising (question, radio button answer 
        // choices, and submit button)
        public class QuestionCardPanel extends JPanel
        {
            public QuestionCardPanel()
            {
                qCards = new CardLayout();
                setLayout(qCards);

                // A default card as placeholder for beginning and in-between the questions.	
                add(makeDefautQuestionPanel(), "defaultQ");
                add(makeWinQuestionPanel(), "Win");
            }

            public void addQuestions()
            {
                QuestionBank bank = getQuestionBank();
                qBankSize = bank.size();
                // Add questions to the panel
                for(int i = 0; i < bank.size(); i++)
                {
                    RadioButtonQuestionPanel rbqp = new RadioButtonQuestionPanel(bank, i);
                    // The card name contains question index to help select right question.
                    add(rbqp, "Q" + Integer.toString(i));
                }
            }
        }
        
        // Determines what question bank to use based off user input.
        public QuestionBank getQuestionBank()
        {
            QuestionBank bank = easyUSABank;

            if(isBeginner && isUSA && isWorld)
                bank = easyBothBank;
            else if(isAdvanced && isUSA && isWorld)
                bank = hardBothBank;
            else if(isBeginner && isUSA)
                bank = easyUSABank;
            else if(isAdvanced && isUSA)
                bank = hardUSABank;
            else if(isBeginner && isWorld)
                bank = easyWorldBank;
            else if(isAdvanced && isWorld)
                bank = hardWorldBank;

            return bank;
        }

        // Standard format for a question.
        public class Question
        {
            String questionPrompt = "";
            String choice1 = "";
            String choice2 = "";
            String choice3 = "";
            String choice4 = "";
            int answerIndex = 0; // correct index starts from 1
            String answerFeedback = "";
        }

        // Question bank for each questions file. Up to 60 questions per bank.
        public class QuestionBank
        {
            private Scanner input; // input scanner
            private String fileName; // file being read
            private int numQuestions; // number of questions in the file
            // Initially questions from files and then scrambled
            private Question[] qBank;
            
            // Defines the questionbank for the file being read. Max 60 ques.
            public QuestionBank(String inFileName)
            {
                fileName = inFileName;
                numQuestions = 0;
                qBank = new Question[60];
            }

            // Reads the question file by calling openIt(). New question starts at "Q: "
            // Gets the question prompt, 4 answer choices, correct answer, and feedback.
            public void readQuestion()
            {
                openIt();

                String line = new String("");
                while(input.hasNextLine())
                {
                    line = input.nextLine();
                    if(line.indexOf("Q:") == 0)
                    {
                        qBank[numQuestions] = new Question();
                        qBank[numQuestions].questionPrompt = line;
                        qBank[numQuestions].choice1 = input.nextLine();
                        qBank[numQuestions].choice2 = input.nextLine();
                        qBank[numQuestions].choice3 = input.nextLine();
                        qBank[numQuestions].choice4 = input.nextLine();
                        qBank[numQuestions].answerIndex = Integer.parseInt(input.nextLine());
                        qBank[numQuestions].answerFeedback = input.nextLine();
                        numQuestions++;
                    }
                }

                // Shuffle the questions from the file that was just read
                shuffleQuestions();
            }

            // returns the question from its index.
            public Question getQuestion(int index)
            {
                if(index >= numQuestions)
                {
                    System.err.printf("\n\nERROR: Index %d more than expected %d. \n\n",
						index, numQuestions);
                    System.exit(1);
                }
                return qBank[index];
            }

            // To shuffle the questions so a random one is given every time.
            private void shuffleQuestions()
            {
                for(int i = 0; i < numQuestions; i++)
                {
                    int toSwap = (int)(Math.random() * (numQuestions));
                    Question temp = qBank[toSwap];
                    qBank[toSwap] = qBank[i];
                    qBank[i] = temp;
                }
            }

            // Size of question bank.
            public int size()
            {
                return numQuestions;
            }

            // readIt uses a try-catch block to open a file
            public void openIt()
            {
                File inFile = new File(fileName);
                try
                {
                    input = new Scanner(inFile);
                }
                catch (FileNotFoundException evt)
                {
                    System.err.printf("\n\nERROR: Cannot find/open file %s. \n\n", inFile);
                    System.exit(1);
                }
            }
        }

        // Question Panel in BorderLayout to show
        // - Question prompt in the north
        // - Answer Choice panel in the center that is in grid layout to hold
		// answer choices radiobuttons
        // - Submit button in the south
        public class RadioButtonQuestionPanel extends JPanel
        {
            private JRadioButton b1, b2, b3, b4; // four answer choices
            private boolean isAnswerSubmitted; // whether the answer has been submitted
            private boolean answer1, answer2, answer3, answer4; // which answer is picked.
            private JButton submitButton; // button to submit answer

            private String choice1, choice2, choice3, choice4;
            private int answerIndex; // the index of the real answer starting with 1

            private String answerTip; // Details of the answer description to educate
            private JFrame answerFrame; // Window to show the answer tip for the question

            // Constructor
            // Input parameter is the index of the question from the question bank
            RadioButtonQuestionPanel(QuestionBank qBank, int index)
            {
                setLayout(new BorderLayout());
                setPreferredSize(new Dimension(350, 500));

                isAnswerSubmitted = false;

                Question question = qBank.getQuestion(index);
                String questionPrompt = question.questionPrompt;
                choice1 = question.choice1;
                choice2 = question.choice2;
                choice3 = question.choice3;
                choice4 = question.choice4;
                answerIndex = question.answerIndex;
                answerTip = question.answerFeedback;

                // The input parameters are for rows/columns not pixels. to be used later.
                JTextArea questionArea = new JTextArea(questionPrompt, 6, 1);
                questionArea.setFont(font3);
                questionArea.setLineWrap(true);
                questionArea.setWrapStyleWord(true);
                questionArea.setOpaque(true);
                questionArea.setEditable(false);
                questionArea.setBackground(VERYLIGHTBLUE);
                add(questionArea, BorderLayout.NORTH);

                // Answer choice panel holding the answer choices as radio buttons in grid layout.
                JPanel choicePanel = new JPanel();
                choicePanel.setLayout(new GridLayout(4, 1, 1, 1));

                // only one radio button can be selected.
                ButtonGroup bg = new ButtonGroup();

                b1 = new JRadioButton(choice1);
                b2 = new JRadioButton(choice2);
                b3 = new JRadioButton(choice3);
                b4 = new JRadioButton(choice4);

                bg.add(b1);
                bg.add(b2);
                bg.add(b3);
                bg.add(b4);

                choiceButtonsHandler bh = new choiceButtonsHandler();
                b1.addActionListener(bh);
                b2.addActionListener(bh);
                b3.addActionListener(bh);
                b4.addActionListener(bh);

                choicePanel.add(b1);
                choicePanel.add(b2);
                choicePanel.add(b3);
                choicePanel.add(b4);
                add(choicePanel, BorderLayout.CENTER);

                // Submit button at the bottom
                submitButton = createButton("Submit!", LIGHTGRAY,
                    LIGHTRED, font3);
                submitButton.setPreferredSize(new Dimension(350, 50));
                submitButtonHandler sbh = new submitButtonHandler();
                submitButton.addActionListener(sbh);
                submitButton.setEnabled(false);
                add(submitButton, BorderLayout.SOUTH);
            } // end of RadioButtonQuestionPanel

            // Listener for the answer choice radio buttons
            // Stores the answer choice picked and controls enabling the submit button
            public class choiceButtonsHandler implements ActionListener
            {
                public void actionPerformed(ActionEvent evt)
                {
                    if(b1.isSelected())
                    {
                        answer1 = true;
                        answer2 = false;
                        answer3 = false;
                        answer4 = false;
                    }
                    
                    if(b2.isSelected())
                    {
                        answer2 = true;
                        answer1 = false;
                        answer3 = false;
                        answer4 = false;
                    }
                    
                    if(b3.isSelected())
                    {
                        answer3 = true;
                        answer1 = false;
                        answer2 = false;
                        answer4 = false;
                    }
                    
                    if(b4.isSelected())
                    {
                        answer4 = true;
                        answer1 = false;
                        answer2 = false;
                        answer3 = false;
                    }

                    if(cellState[currentCellRow][currentCellColumn] < 2)
                    {
                        submitButton.setBackground(LIGHTGREEN);
                        submitButton.setEnabled(true);
                    }
                }
            }

            // Listener for the submit button
            // Determine if the answer is right, update cell color, and show the
            // AnswerTip window.
            // Updates cells with appropriate images based on certain actions
            // (correct, incorrect, treasure cell clicked)
            public class submitButtonHandler implements ActionListener
            {
                public void actionPerformed(ActionEvent evt)
                {
                    isAnswerSubmitted = true;
                    submitButton.setEnabled(false);

                    boolean correct = false;
                    if(answer1 && answerIndex == 1)
                        correct = true;
                    else if(answer2 && answerIndex == 2)
                        correct = true;
                    else if(answer3 && answerIndex == 3)
                        correct = true;
                    else if(answer4 && answerIndex == 4)
                        correct = true;
                    else
                        correct = false;

                    if(correct)
                    {
                        // Update cell state of the clicked cell to right answer
                        cellState[currentCellRow][currentCellColumn] = 2;
                        JButton currentCell = getButtonXAndY(gPanel,
                            currentCellRow, currentCellColumn);
                        if(currentCellRow == 0 && currentCellColumn == COLUMN - 1)
                        {
							currentCell.setHorizontalAlignment(SwingConstants.LEFT);
                            int width = currentCell.getWidth();
							int height = currentCell.getHeight();
							currentCell.setIcon(null);
							currentCell.setIcon(new ImageIcon(treasureImage.getScaledInstance(width,
								height, java.awt.Image.SCALE_SMOOTH)));
                            lifeButton.setEnabled(false);
                        }
                        else
                        {
							currentCell.setHorizontalAlignment(SwingConstants.LEFT);
							int width = currentCell.getWidth();
							int height = currentCell.getHeight();
							currentCell.setIcon(new ImageIcon(pathImage.getScaledInstance(width,
								height, java.awt.Image.SCALE_SMOOTH)));
						}
                        currentCell.setBackground(LIGHTGREEN);
                    }
                    else 
                    {					
                        // Update cell state of the clicked cell to wrong answer
                        cellState[currentCellRow][currentCellColumn] = 3;
                        JButton currentCell = getButtonXAndY(gPanel, currentCellRow,
							currentCellColumn);
                        
                        currentCell.setHorizontalAlignment(SwingConstants.LEFT);
                        int width = currentCell.getWidth();
                        int height = currentCell.getHeight();
                        ImageIcon brickIcon = new ImageIcon(brickImage.getScaledInstance(width,
							height, java.awt.Image.SCALE_SMOOTH));
                        currentCell.setIcon(brickIcon);
						lifeButton.setEnabled(true);
                    }

                    showAnswerTipWindow(correct);
                    pStats.updateStats(correct);
                    String statsText = " " + playerName + "'s Stats";
                    statsText = statsText + pStats.getStatsText();
                    playerStatsText.setText(statsText);
                }
            }

            // Separate JFrame for Answer feedback
            public void showAnswerTipWindow(boolean isCorrect)
            {
                // if already open, bring the window to focus
                if(isFeedbackFrameOpen)
                {
                    answerFrame.requestFocus();
                    answerFrame.toFront();
                }
                else
                {
                    createAnswerFrame(isCorrect);
                }
            }

            // Creates JFrame window 
            // Input parameter determines the background color of window panel
            public void createAnswerFrame(boolean isCorrect)
            {
                answerFrame = new JFrame("Answer Tip");
                answerFrame.setSize(400, 300);
                answerFrame.setLocation(810, 100);
                answerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                answerFrame.setResizable(false);
                WindowListenerClass windowL = new WindowListenerClass();
                answerFrame.addWindowListener(windowL);
                answerFrame.add(makeAnswerTipPanel(isCorrect));
                answerFrame.setVisible(true);
            }

            public class WindowListenerClass implements WindowListener
            {
                // disable home button if the answer feedback window is open
                public void windowOpened(WindowEvent evt)
                {
                    isFeedbackFrameOpen = true;
                }

                // Show default card when the Answer feedback window is closed			
                public void windowClosing(WindowEvent evt)
                {
                    isFeedbackFrameOpen = false;

                    qCards.show(qcp, "defaultQ");
                    isQuestionOpen = false;
                    currentCellRow = -1;
                    currentCellColumn = -1;
                }

                public void windowClosed(WindowEvent evt) {};
                public void windowIconified(WindowEvent evt) {};
                public void windowDeiconified(WindowEvent evt) {};
                public void windowActivated(WindowEvent evt) {};
                public void windowDeactivated(WindowEvent evt) {};
            }

            // creates JPanel to hold the Answer feedback
            // Input parameter isCorrect determines the content and color.
            public JPanel makeAnswerTipPanel(boolean isCorrect)
            {
                JPanel tipPanel = new JPanel();
                tipPanel.setLayout(new BorderLayout());

                String Tip = "";
                if(isCorrect)
                    Tip = "Congratulations! Your answer is correct.";
                else
                {
                    String correctAnswer = "";

                    if(answerIndex == 1)
                        correctAnswer = choice1;
                    else if(answerIndex == 2)
                        correctAnswer = choice2;
                    else if(answerIndex == 3)
                        correctAnswer = choice3;
                    else if(answerIndex == 4)
                        correctAnswer = choice4;

                    Tip = "Sorry! Your answer is wrong.\n\nThe correct answer is " +
                        correctAnswer + ".\nTip: " + answerTip;
                }

                Tip += "\n\nPlease close the window to proceed!";

                // Holds the answer feedback
                JTextArea answerTip = new JTextArea(Tip, 4, 1);
                answerTip.setMargin(new Insets(0, 5, 0, 5));
                answerTip.setFont(new Font("Tahoma", Font.PLAIN, 17));
                answerTip.setLineWrap(true);
                answerTip.setWrapStyleWord(true);
                answerTip.setOpaque(true);
                answerTip.setEditable(false);

                if(isCorrect)
                    answerTip.setBackground(LIGHTGREEN);
                else
                    answerTip.setBackground(VERYLIGHTRED);
                tipPanel.add(answerTip, BorderLayout.CENTER);

                return tipPanel;
            }
        }

        // Make the default question panel as a placeholder before user clicks on a grid cell
        public JPanel makeDefautQuestionPanel()
        {
            JPanel dQPanel = new JPanel();
            dQPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 150));
            dQPanel.setPreferredSize(new Dimension(350, 50));
            dQPanel.setBackground(VERYLIGHTYELLOW);

            JButton defaultButton = createButton("Select an open cell", LIGHTYELLOW,
                LIGHTRED, font3);
            defaultButton.setPreferredSize(new Dimension(200, 50));
            dQPanel.add(defaultButton);

            return dQPanel;
        }

        // Make the winning panel when the user has reached the Treasure
        public JPanel makeWinQuestionPanel()
        {
            JPanel wQPanel = new JPanel();
            wQPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 100));
            wQPanel.setPreferredSize(new Dimension(350, 50));
            wQPanel.setBackground(VERYLIGHTYELLOW);

            JTextArea winTextArea = new JTextArea("You found the Treasure!\n\nClick the " +
                "Leaderboard button at the bottom to see your results compared to others.", 8, 3);
            winTextArea.setFont(font5);
            winTextArea.setLineWrap(true);
            winTextArea.setWrapStyleWord(true);
            winTextArea.setOpaque(true);
            winTextArea.setEditable(false);
            winTextArea.setPreferredSize(new Dimension(300, 50));
            winTextArea.setBackground(VERYLIGHTRED);
            wQPanel.add(winTextArea);

            return wQPanel;
        }

        // Randomly get a question from the question bank size
        public int getRadioQuestionIndex(int size)
        {
            int index = 0;
            if(numQuestionsShown >= size)
            {
                // Potentially duplicate as all questions are exhausted.
                index = (int)(Math.random() * size);
            }
            else
            {
                int eligible[] = new int[size - numQuestionsShown];

                int j = 0;
                for(int i = 0; i < size; i++)
                {
                    if(!isAlreadyShown(i))
                    {
                        eligible[j] = i;
                        j++;
                    }
                }
                index = eligible[(int)(Math.random() * j)];
            }

            questionShownIndices[numQuestionsShown] = index;
            numQuestionsShown++;
            return index;
        }
        
        // If the question has been already shown (from index)
        public boolean isAlreadyShown(int index)
        {
            for(int i = 0; i < numQuestionsShown; i++)
            {
                if(questionShownIndices[i] == index)
                    return true;
            }
            return false;
        }

        // Make the bottom panel in the play area for navigation and stats.
        public JPanel makePlayBottomPanel()
        {
            JPanel playBottomPanel = new JPanel();
            playBottomPanel.setLayout(new BorderLayout());

            JPanel componentsPanel = new JPanel();
            componentsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

            playerStatsText = new JTextArea(" " + playerName + "'s Stats" +
                pStats.getStatsText(), 5, 3);
            playerStatsText.setFont(font4);
            playerStatsText.setLineWrap(true);
            playerStatsText.setWrapStyleWord(true);
            playerStatsText.setOpaque(true);
            playerStatsText.setEditable(false);
            playerStatsText.setPreferredSize(new Dimension(350, 50));
            playerStatsText.setBackground(VERYLIGHTRED);
            componentsPanel.add(playerStatsText);

            lifeButton = createButton("Use LifeLine", LIGHTRED, LIGHTYELLOW, font3);
            lifeButton.setPreferredSize(new Dimension(150, 50));
            lifeButtonHandler lbh = new lifeButtonHandler();
            lifeButton.addActionListener(lbh);
            lifeButton.setEnabled(false);
            componentsPanel.add(lifeButton);

            playBottomPanel.add(componentsPanel, BorderLayout.CENTER);

            homeButton = createButton("Restart", LIGHTYELLOW, BROWN, font3);
            homeButton.setPreferredSize(new Dimension(130, 50));
            reStartButtonHandler homebh = new reStartButtonHandler();
            homeButton.addActionListener(homebh);
            playBottomPanel.add(homeButton, BorderLayout.WEST);

            rankingButton = createButton("Leaderboard", VERYLIGHTGREEN,
				LIGHTBLUE, font3);
            rankingButton.setPreferredSize(new Dimension(140, 50));
            rankingButtonHandler rbh = new rankingButtonHandler();
            rankingButton.addActionListener(rbh);
            rankingButton.setEnabled(true);

            playBottomPanel.add(rankingButton, BorderLayout.EAST);

            return playBottomPanel;
        }

        // Handler class for the LifeLine button. Clears all blocked cells
        // and reverts them back to an unopened cell.
        public class lifeButtonHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                for(int i = 0; i < ROW; i++)
                {
                    for(int j = 0; j < COLUMN; j++)
                    {
                        if(cellState[i][j] == 3)  // answered wrong
                        {
							cellState[i][j] = 0;
							JButton cellButton = getButtonXAndY(gPanel, i, j);
                            cellButton.setBackground(LIGHTYELLOW);
                            cellButton.setHorizontalAlignment(SwingConstants.CENTER);
                            cellButton.setText("?");
                            cellButton.setIcon(null);
                        }
                    }
                }
                pStats.useLife();
                playerStatsText.setText(" " + playerName + "'s Stats" + pStats.getStatsText());
            }
        }

        // Listener for the randking button. Resets Player data. Still tentative.
        public class rankingButtonHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
				// Select beginner or advanced leaderboard file based on player's level.
                LeaderBoardStats lb = new LeaderBoardStats(getLeaderFile());
                lb.readLeaderBoard();
                // Get the stats of top players, format it, and add to the textArea
                rankingArea.setText(lb.getStatsText());

                cards.show(cp, "LeaderBoard");
            }
        }

        // Player stats class for showing the current player stats during gameplay.
        public class playerStats
        {
            private int numQuestionsAttempted; // # of questions the player has attempted
            private int numQuestionsCorrect; // # of questions the player has gotten correct
            private int numQuestionsWrong; // # of questions the player has gotten wrong
            private boolean isLifeLineUsed; // if the player has used a LifeLine

            // Default stats for before player starts.
            public playerStats()
            {
                isLifeLineUsed = false;
                numQuestionsAttempted = 0;
                numQuestionsCorrect = 0;
                numQuestionsWrong = 0;
            }

            // Updates the stats in real time. Counts for questions attempted, correct,
            // and incorrect.
            public void updateStats(boolean isCorrect)
            {
                numQuestionsAttempted++;

                if(isCorrect)
                    numQuestionsCorrect++;
                else
                    numQuestionsWrong++;
            }

            // Called from the LifeLine button handler. True when LifeLine button clicked.
            public void useLife()
            {
                isLifeLineUsed = true;
            }

            // Gets the stats from the stats text area.
            public String getStatsText()
            {
                String statsText = "";
                statsText += "\n Questions attempted: " + Integer.toString(numQuestionsAttempted) +
                    "\n Questions correct: " + Integer.toString(numQuestionsCorrect) +
                    "\n Questions wrong: " + Integer.toString(numQuestionsWrong);

                if(isLifeLineUsed)
                {
                    statsText += "\n LifeLine used: Yes";
                    lifeButton.setEnabled(false);
                }
                else
                {
                    statsText += "\n LifeLine used: No";
                }

                return statsText;
            }

            // Raw stats used to write to the Leaderboard text file. Done much simpilar
            // so it is easier to work with.
            public String getRawStatsText()
            {
                String rawStats = "";
                rawStats += Integer.toString(numQuestionsAttempted) + "\n";
                rawStats += Integer.toString(numQuestionsCorrect) + "\n";
                rawStats += Integer.toString(numQuestionsWrong) + "\n";

                if(isLifeLineUsed)
                    rawStats += "1\n";
                else
                    rawStats += "0\n";

                return rawStats;
            }
        }

        // Writes the player stats to the Leaderboard file
        // Formatted from rawStats
        public void writeStatsToFile()
        {
            writeStatsText = "Player Name: " + playerName;
            if(isAdvanced)
                writeStatsText += "\n1\n";
            else
                writeStatsText += "\n0\n";

            if(isUSA && isWorld)
                writeStatsText += "2\n";
            else if(isUSA)
                writeStatsText += "0\n";
            else if(isWorld)
                writeStatsText += "1\n";

            int startIndex = playerTimeText.indexOf("[");
            int endIndex = playerTimeText.indexOf("]");
            writeStatsText += playerTimeText.substring(startIndex + 1, endIndex) + "\n";
            writeStatsText += pStats.getRawStatsText();
            writeStatsText += "\n";
            checkWriteToFile();
        }

        // Try-catch method to check if it can write to the Leaderboard file.
        // Appends stats and closes file.
        public void checkWriteToFile()
        {
            PrintWriter outFile = null;
            try
            {
                FileWriter fileWriter = new FileWriter(getLeaderFile(), true);
                outFile = new PrintWriter(fileWriter);
            }
            catch (IOException evt)
            {
                evt.printStackTrace();
                System.exit(1);
            }
            outFile.append(writeStatsText);
            outFile.close();
        }
    } // end of Game Play
    
    // Leaderboard class for the leaderboard. Ranking and stats of player.
    // Reads from player's stats and writes it to a text area.
    public class LeaderBoard extends JPanel
    {
        public LeaderBoard()
        {
            setLayout(new BorderLayout());

            leaderLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
            leaderLabel.setForeground(Color.BLACK);
            leaderLabel.setFont(font5);
            leaderLabel.setPreferredSize(new Dimension(500, 70));
            leaderLabel.setBackground(GOLD);
            leaderLabel.setOpaque(true);
            add(leaderLabel, BorderLayout.NORTH);

            JButton emptyLeftButton = createButton("", VERYLIGHTRED, VERYLIGHTRED, font3);
            emptyLeftButton.setPreferredSize(new Dimension(50, 50));
            add(emptyLeftButton, BorderLayout.WEST);

            JButton emptyRightButton = createButton("", VERYLIGHTRED, VERYLIGHTRED, font3);
            emptyRightButton.setPreferredSize(new Dimension(50, 50));
            add(emptyRightButton, BorderLayout.EAST);

            rankingArea = new JTextArea("LeaderBoard Stats", 6, 1);
            rankingArea.setFont(font3);
            rankingArea.setLineWrap(true);
            rankingArea.setWrapStyleWord(true);
            rankingArea.setOpaque(true);
            rankingArea.setEditable(false);
            rankingArea.setBackground(VERYLIGHTBLUE);
            add(rankingArea, BorderLayout.CENTER);

            JPanel leaderBottomPanel = new JPanel();
            leaderBottomPanel.setLayout(new BorderLayout());

            JButton goHomeButton = createButton("Go Home!", LIGHTYELLOW, BROWN, font3);
            goHomeButton.setPreferredSize(new Dimension(120, 70));
            reStartButtonHandler rbh = new reStartButtonHandler();
            goHomeButton.addActionListener(rbh);
            leaderBottomPanel.add(goHomeButton, BorderLayout.WEST);

            JButton replayButton = createButton("Replay!", LIGHTYELLOW, BROWN, font3);
            replayButton.setPreferredSize(new Dimension(130, 70));
            replayGameButtonHandler rgbh = new replayGameButtonHandler();
            replayButton.addActionListener(rgbh);
            leaderBottomPanel.add(replayButton, BorderLayout.EAST);
            
			leaderBottomPanel.setBackground(VERYLIGHTBLUE);
            add(leaderBottomPanel, BorderLayout.SOUTH);
        }
        
        // Clears all data when replay button is clicked.
        public class replayGameButtonHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
            elapsedTime = 0;
            timer.stop();
            playerTimeText = "  [00:00:00]";
            playerName = "";

            cp.add(new GameSettings(), "Game Settings");
            cp.add(new GamePlay(), "Play Page");

            cards.show(cp, "Game Settings");
			}
        }
    } // end of Leaderboard

    // Standard format for stats for a player.
    public class LeaderStats
    {
        String name = ""; // player name
        int wasAdvanced = 0; // if the player chose advance difficulty
        int area; // 0 for USA, 1 for World, and 2 for both.
        String time = "00:00:00"; // player's time
        int attempts; // # of attempted questions
        int correct; // # of correct questions
        int wrong; // # of incorrect questions
        int usedLifeLine = 0; // if the player used a lifeline
        double perCorrect; // percentage of the correct answers (correct/attempts)
    }

    // Question bank for each questions file. Up to 200 questions.
    public class LeaderBoardStats
    {
        private Scanner input;
        private String fileName;
        private int numLeaders;
        // Stores the leaderboard stats read from the file.
        private LeaderStats lStats[];

        public LeaderBoardStats(String inFileName)
        {
            fileName = inFileName;
            numLeaders = 0;
            lStats = new LeaderStats[200];
        }

        // Reads the question file. New question starts at "Q: "
        // Gets the question prompt, 4 answer choices, correct answer, and feedback.
        public void readLeaderBoard()
        {
            openIt();

            String line = new String("");
            while(input.hasNextLine())
            {
                line = input.nextLine();
                if(line.indexOf("Player Name:") == 0)
                {
					// new leaderstats class for player
                    lStats[numLeaders] = new LeaderStats();
                    lStats[numLeaders].name = line.substring(line.indexOf(":") + 2);
                    lStats[numLeaders].wasAdvanced = Integer.parseInt(input.nextLine());
                    lStats[numLeaders].area = Integer.parseInt(input.nextLine());
                    lStats[numLeaders].time = input.nextLine();
                    lStats[numLeaders].attempts = Integer.parseInt(input.nextLine());
                    lStats[numLeaders].correct = Integer.parseInt(input.nextLine());
                    lStats[numLeaders].wrong = Integer.parseInt(input.nextLine());
                    lStats[numLeaders].usedLifeLine = Integer.parseInt(input.nextLine());
                    lStats[numLeaders].perCorrect = (double) lStats[numLeaders].correct *
                        100.0 / lStats[numLeaders].attempts;

                    numLeaders++;
                }
            }
            
            sort(); // sorts players in leaderboard by percentage correct answers
        }

        // Sorts the players in the leaderboard file. Iterates through each player
        // to check who has the higher percentage of questions correct.
        public void sort()
        {
            for(int i = 0; i < numLeaders; i++)
            {
                for(int j = i + 1; j < numLeaders; j++)
                {
                    LeaderStats tmp = new LeaderStats();
                    if(lStats[i].perCorrect < lStats[j].perCorrect)
                    {
                        tmp = lStats[i];
                        lStats[i] = lStats[j];
                        lStats[j] = tmp;
                    }
                }
            }
        }
        
        // Writes the player stats to the text area. shows top 20 players.
        // formats while writing.
        public String getStatsText()
        {
            String sText = "";
            sText += String.format("%-5s\t%-20s%-20s%-10s\t%-9s\n", "Rank",
                "Name", "Learning Area", "Time", "Correct %");

            int maxLeaders = numLeaders;
            if(maxLeaders > 15)
				maxLeaders = 15;

            for(int i = 0; i < maxLeaders; i++)
            {
                String lArea = "";
                if(lStats[i].area == 0)
                    lArea = "USA";
                else if(lStats[i].area == 1)
                    lArea = "World";
                else if(lStats[i].area == 2)
                    lArea = "Both USA and World";

                sText += String.format("%-5d\t%-20s%-20s%-10s\t%5.2f%%\n",
                    i + 1, lStats[i].name, lArea, lStats[i].time, lStats[i].perCorrect);
            }
            return sText;
        }

        // Size of question bank.
        public int size()
        {
            return numLeaders;
        }

        // readIt uses a try-catch block to open a file
        public void openIt()
        {
            File inFile = new File(fileName);
            try
            {
                input = new Scanner(inFile);
            }
            catch (FileNotFoundException evt)
            {
                System.err.printf("\n\nERROR: Cannot find/open file %s. \n\n", inFile);
                System.exit(1);
            }
        }
    } // end of LeaderBoardStats	

    // This makes a popup frame/window with a card layout and instructions for 
    // different aspects of the game.
    public class instructionButtonHandler implements ActionListener
    {
        private CardPanelI cpi; // instructions panel is in card layout
        private CardLayout cardsI; // cards in the card layout

        // Instruction frame popup
        public void actionPerformed(ActionEvent evt)
        {
            if(isInstructionWindowOpen)
            {
                iFrame.requestFocus();
                iFrame.toFront();
            }
            else
                createInstructionFrame();
        }

        // Creates the instruction frame.
        public void createInstructionFrame()
        {
            iFrame = new JFrame("How to Play GeoMazer");
            iFrame.setSize(550, 700);
            iFrame.setLocation(815, 0);
            iFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            iFrame.setResizable(false);
            WindowInstructionListenerClass windowIL = new WindowInstructionListenerClass();
            iFrame.addWindowListener(windowIL);
            iFrame.setVisible(true);

            cpi = new CardPanelI();
            iFrame.add(cpi);

            iFrame.setVisible(true);
        }
        
        // Window listener class to open and close the instructions window
        public class WindowInstructionListenerClass implements WindowListener
        {
            public void windowOpened(WindowEvent evt)
            {
                isInstructionWindowOpen = true;
            }

            public void windowClosing(WindowEvent evt)
            {
                isInstructionWindowOpen = false;
            }

            public void windowClosed(WindowEvent evt) {};
            public void windowIconified(WindowEvent evt) {};
            public void windowDeiconified(WindowEvent evt) {};
            public void windowActivated(WindowEvent evt) {};
            public void windowDeactivated(WindowEvent evt) {};
        }

        public class CardPanelI extends JPanel
        {
            // For all the pages in the instructions window
            public CardPanelI()
            {
                cardsI = new CardLayout();
                setLayout(cardsI);

                HowToPlayPanel htpp = new HowToPlayPanel();
                HowToWinPanel htwp = new HowToWinPanel();
                LifeLinePanel llp = new LifeLinePanel();

                add(htpp, "How to Play");
                add(htwp, "How to Win");
                add(llp, "What is a LifeLine?");
            }
        }

        public class HowToPlayPanel extends JPanel
        {
            // first page in instructions. left and right buttons.
            public HowToPlayPanel()
            {
                setLayout(new BorderLayout());

                JLabel howLabel = new JLabel("How to Play", SwingConstants.CENTER);
                howLabel.setFont(font2);
                howLabel.setPreferredSize(new Dimension(800, 50));
                add(howLabel, BorderLayout.NORTH);

				JPanel playInstruction = new JPanel();
                playInstruction.setLayout(new BorderLayout());

				String text = "A player starts from the “Start” cell on the board. The player " + 
				"may only move up, right, down, or left. To show all available paths that are possible " +
				"from a cell, hold either left or right-click on the cell that you want to proceed from.\n" + 
				"Clicking a valid cell will prompt a question in the question panel. If answered correctly, " +
				"the path is built and shown with a checkmark icon. If answered incorrectly, the cell is " +
				"blocked and another route must be taken to reach the treasure." +
				"An answer tip is given as well to give them the correct answer and " + 
				"teach them how to remember the answer.";
				
				JTextArea hText = new JTextArea(text, 9, 15);
				hText.setMargin(new Insets(0, 5, 0, 5));
				hText.setFont(new Font("Tahoma", Font.PLAIN, 16));
				hText.setLineWrap(true);
				hText.setWrapStyleWord(true);
				hText.setOpaque(true);
				hText.setEditable(false);
				hText.setBackground(VERYLIGHTGREEN);
				playInstruction.add(hText, BorderLayout.NORTH);

				Image howPlayImage = getMyImage("./pictures/" + "playImage.png");
				ImageIcon howPlayIcon = new ImageIcon(howPlayImage.getScaledInstance(400,
					400, Image.SCALE_DEFAULT));
				JButton howPlayButton = new JButton(howPlayIcon);
				howPlayButton.setBackground(Color.WHITE);
				howPlayButton.setOpaque(true);
				howPlayButton.setBorderPainted(false);
				playInstruction.add(howPlayButton, BorderLayout.SOUTH);
				
                add(playInstruction, BorderLayout.CENTER);
                
                JPanel howToPlayControl = new JPanel();
                howToPlayControl.setLayout(new BorderLayout());

                JButton rightButton = new JButton("=>");
                rightButton.setBackground(VERYLIGHTGREEN);
                rightButton.setOpaque(true);
                rightButtonHandler rbh = new rightButtonHandler();
                rightButton.addActionListener(rbh);
                howToPlayControl.add(rightButton, BorderLayout.EAST);
                
                add(howToPlayControl, BorderLayout.SOUTH);
            }

            public class rightButtonHandler implements ActionListener
            {
                // goes to next page
                public void actionPerformed(ActionEvent evt)
                {
                    cardsI.show(cpi, "How to Win");
                }
            }
        }

        public class HowToWinPanel extends JPanel
        {
            // second page in instructions. left and right buttons.
            public HowToWinPanel()
            {
                setLayout(new BorderLayout());

                JLabel winLabel = new JLabel("How to Win", SwingConstants.CENTER);
                winLabel.setFont(font2);
                winLabel.setPreferredSize(new Dimension(800, 50));
                add(winLabel, BorderLayout.NORTH);
                
                JPanel winInstruction = new JPanel();
                winInstruction.setLayout(new BorderLayout());

				String text = "\nA player wins when they have created a path to reach the treasure. " + 
				"Upon clicking the treasure cell, the game ends and the player may access the leaderboard " +
				"page to view the top scores (ranked by the % of questions correct) in their difficulty " +
				"category - Beginner or Advanced. The leaderboard shows the " + 
				"top 15 players for that respective difficulty.";
				JTextArea wText = new JTextArea(text, 8, 15);
				wText.setMargin(new Insets(0, 10, 0, 10));
				wText.setFont(new Font("Tahoma", Font.PLAIN, 18));
				wText.setLineWrap(true);
				wText.setWrapStyleWord(true);
				wText.setOpaque(true);
				wText.setEditable(false);
				wText.setBackground(VERYLIGHTGREEN);
				winInstruction.add(wText, BorderLayout.NORTH);

				Image winPlayImage = getMyImage("./pictures/" + "winImage.png");
				ImageIcon winPlayIcon = new ImageIcon(winPlayImage.getScaledInstance(400,
					400, Image.SCALE_DEFAULT));
				JButton winPlayButton = new JButton(winPlayIcon);
				winPlayButton.setBackground(Color.WHITE);
				winPlayButton.setOpaque(true);
				winPlayButton.setBorderPainted(false);
				winInstruction.add(winPlayButton, BorderLayout.SOUTH);
                add(winInstruction, BorderLayout.CENTER);

                JPanel howToWinControl = new JPanel();
                howToWinControl.setLayout(new BorderLayout());
                add(howToWinControl, BorderLayout.SOUTH);

                JButton rightButton = new JButton("=>");
                rightButtonHandler rbh = new rightButtonHandler();
                rightButton.setBackground(VERYLIGHTGREEN);
                rightButton.setOpaque(true);
                
                rightButton.addActionListener(rbh);
                howToWinControl.add(rightButton, BorderLayout.EAST);

                JButton leftButton = new JButton("<=");
                leftButton.setBackground(VERYLIGHTGREEN);
                leftButton.setOpaque(true);
                
                leftButtonHandler lbh = new leftButtonHandler();
                leftButton.addActionListener(lbh);
                howToWinControl.add(leftButton, BorderLayout.WEST);
            }

            public class rightButtonHandler implements ActionListener
            {
                // goes to next page
                public void actionPerformed(ActionEvent evt)
                {
                    cardsI.show(cpi, "What is a LifeLine?");
                }
            }

            public class leftButtonHandler implements ActionListener
            {
                // goes to previous page
                public void actionPerformed(ActionEvent evt)
                {
                    cardsI.show(cpi, "How to Play");
                }
            }
        }

        public class LifeLinePanel extends JPanel
        {
            // third page in instructions. left button only.
            public LifeLinePanel()
            {
                setLayout(new BorderLayout());

                JLabel lifeLineLabel = new JLabel("What is a LifeLine?",
					SwingConstants.CENTER);
                lifeLineLabel.setFont(font2);
                lifeLineLabel.setPreferredSize(new Dimension(800, 50));
                add(lifeLineLabel, BorderLayout.NORTH);
                
                JPanel lifeInstruction = new JPanel();
                lifeInstruction.setLayout(new BorderLayout());

				String text = "\n\nA lifeline is best used when all available " +
				"paths have been blocked by brick walls. It clears up all the " + 
				"blocked cells, letting you to proceed from your current path(s).\n" +
				"However, a lifeline may only be used once so use it wisely!";
				JTextArea lText = new JTextArea(text, 8, 15);
				lText.setMargin(new Insets(0, 10, 0, 10));
				lText.setFont(new Font("Tahoma", Font.PLAIN, 18));
				lText.setLineWrap(true);
				lText.setWrapStyleWord(true);
				lText.setOpaque(true);
				lText.setEditable(false);
				lText.setBackground(VERYLIGHTGREEN);
				lifeInstruction.add(lText, BorderLayout.NORTH);
				
				Image howLifeImage = getMyImage("./pictures/" + "lifeImage.png");
				ImageIcon howLifeIcon = new ImageIcon(howLifeImage.getScaledInstance(400,
					400, Image.SCALE_DEFAULT));
				JButton howLifeButton = new JButton(howLifeIcon);
				howLifeButton.setBackground(Color.WHITE);
				howLifeButton.setOpaque(true);
				howLifeButton.setBorderPainted(false);
				lifeInstruction.add(howLifeButton, BorderLayout.SOUTH);
			
                add(lifeInstruction, BorderLayout.CENTER);
				
				JPanel lifeLineControl = new JPanel();
                lifeLineControl.setLayout(new BorderLayout());

                JButton closeInstructions = createButton("Close", Color.RED,
					LIGHTRED, font3);
                closeInstructionsButtonHandler cibh = new closeInstructionsButtonHandler();
                closeInstructions.addActionListener(cibh);
                closeInstructions.setPreferredSize(new Dimension(80, 35));
                lifeLineControl.add(closeInstructions, BorderLayout.EAST);

                JButton leftButton = new JButton("<=");
                
                leftButtonHandler lbh = new leftButtonHandler();
                leftButton.setBackground(VERYLIGHTGREEN);
                leftButton.setOpaque(true);
                leftButton.addActionListener(lbh);
                lifeLineControl.add(leftButton, BorderLayout.WEST);
                
                add(lifeLineControl, BorderLayout.SOUTH);
            }

            public class closeInstructionsButtonHandler implements ActionListener
            {
                // closes instructions window
                public void actionPerformed(ActionEvent evt)
                {
                    iFrame.dispatchEvent(new WindowEvent(iFrame, WindowEvent.WINDOW_CLOSING));
                }
            }

            public class leftButtonHandler implements ActionListener
            {
                // goes to previous page
                public void actionPerformed(ActionEvent evt)
                {
                    cardsI.show(cpi, "How to Win");
                }
            }
        }
    } // end of instructionButtonHandler

    public class PurposePanel extends JPanel
    {
		// Purpose of GeoMazer. Seperate card.
        public PurposePanel()
        {
            setLayout(new BorderLayout());

            JLabel purposeLabel = new JLabel("Why Play GeoMazer?", SwingConstants.CENTER);
            purposeLabel.setForeground(Color.BLACK);
            purposeLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
            purposeLabel.setPreferredSize(new Dimension(500, 100));
            purposeLabel.setBackground(LIGHTRED);
            purposeLabel.setOpaque(true);
            add(purposeLabel, BorderLayout.NORTH);

			String ptext = "\n\n- GeoMazer is a new and unique game that helps you learn " + 
			"Geography as you pursue an adventurous journery to a treasure chest.\n\n" +
			"- Answer a question correctly and advance to a cell closer to the Treasure.\n" +
			"- Get educated when you fail. \n" +
			"- Repitition is the key. Strengthen your knowledge with the Replay option.\n" +
			"- Double fun with combined USA and World learning areas.\n" +
			"- Challenge yourself with the advanced learner level!\n\n" +
			"- The BEST Part!!! Show off your rank on the Leaderboard and earn some bragging rights!";
			
            JTextArea purposeText = new JTextArea(ptext, 5, 1);
            purposeText.setMargin(new Insets(0, 10, 0, 10));
            purposeText.setFont(new Font("Tahoma", Font.PLAIN, 22));
            purposeText.setLineWrap(true);
            purposeText.setWrapStyleWord(true);
            purposeText.setOpaque(true);
            purposeText.setEditable(false);
            purposeText.setBackground(VERYLIGHTBLUE);
            add(purposeText, BorderLayout.CENTER);

            JPanel mpbp = makePurposeBottomPanel();
            add(mpbp, BorderLayout.SOUTH);
        }

        // Bottom panel of the game settings page allows user to navigate.
        public JPanel makePurposeBottomPanel()
        {
            JPanel purposeBottomPanel = new JPanel();
            purposeBottomPanel.setLayout(new BorderLayout());

            JButton homeButton = new JButton("Back Home!");
            homeButtonHandler homebh = new homeButtonHandler();
            homeButton.addActionListener(homebh);
            homeButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
            homeButton.setBackground(LIGHTRED);
            homeButton.setOpaque(true);
            homeButton.setBorderPainted(false);
            purposeBottomPanel.add(homeButton, BorderLayout.WEST);

            JLabel bottomLabel = new JLabel("", SwingConstants.CENTER);
            bottomLabel.setForeground(Color.BLACK);
            bottomLabel.setPreferredSize(new Dimension(500, 100));
            bottomLabel.setBackground(VERYLIGHTBLUE);
            bottomLabel.setOpaque(true);
            purposeBottomPanel.add(bottomLabel, BorderLayout.CENTER);

            return purposeBottomPanel;
        }
    }

    public class homeButtonHandler implements ActionListener
    {
        // goes back to home page
        public void actionPerformed(ActionEvent evt)
        {
            cards.show(cp, "Home Page");
        }
    }

    // Listener for the restart button. Resets Player data. Still tentative.
    public class reStartButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            elapsedTime = 0;
            timer.stop();
            playerTimeText = "  [00:00:00]";
            playerName = "";

            cp.add(new GameSettings(), "Game Settings");
            cp.add(new GamePlay(), "Play Page");

            cards.show(cp, "Home Page");
        }
    }
    
    // Credits page for game.
    public class Credits extends JPanel
    {
        public Credits()
        {
            setLayout(new BorderLayout());

            JLabel creditsLabel = new JLabel("GeoMazer - Credits", SwingConstants.CENTER);
            creditsLabel.setForeground(Color.BLACK);
            creditsLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
            creditsLabel.setPreferredSize(new Dimension(500, 100));
            creditsLabel.setBackground(LIGHTRED);
            creditsLabel.setOpaque(true);
            add(creditsLabel, BorderLayout.NORTH);

			String ctext = "\n\n\n\n\nGeoMazer was created by a Freshman at Monta " +
            "Vista High School who goes by the name \"Aryan Singhal.\" With " +
            "the help of his amazing Java teacher, Mr. Conlin, and his TAs, " +
            "he was able to create this awesome game you are playing right now. " +
            "Not to forget, Aryan would like to thank his parents, and all of his " + 
            "friends and peers for motivating him.";
			
            JTextArea creditsText = new JTextArea(ctext, 5, 1);
            creditsText.setMargin(new Insets(0, 10, 0, 10));
            creditsText.setFont(new Font("Tahoma", Font.PLAIN, 22));
            creditsText.setLineWrap(true);
            creditsText.setWrapStyleWord(true);
            creditsText.setOpaque(true);
            creditsText.setEditable(false);
            creditsText.setBackground(VERYLIGHTBLUE);
            add(creditsText, BorderLayout.CENTER);

            JPanel mcbp = makeCreditsBottomPanel();
            add(mcbp, BorderLayout.SOUTH);
        }

        // Bottom panel of the game settings page allows user to navigate.
        public JPanel makeCreditsBottomPanel()
        {
            JPanel creditsBottomPanel = new JPanel();
            creditsBottomPanel.setLayout(new BorderLayout());

            JButton homeButton = new JButton("Back Home!");
            homeButtonHandler homebh = new homeButtonHandler();
            homeButton.addActionListener(homebh);
            homeButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
            homeButton.setBackground(LIGHTRED);
            homeButton.setOpaque(true);
            homeButton.setBorderPainted(false);
            creditsBottomPanel.add(homeButton, BorderLayout.WEST);
           
            String cbText = ("\n\n\n\n\n\t\t\t\tCopyright: © 2022, GeoMazer.");
           
            JTextArea creditsBottomArea = new JTextArea(cbText, 5, 1);
            creditsBottomArea.setForeground(Color.BLACK);
            creditsBottomArea.setPreferredSize(new Dimension(500, 100));
            creditsBottomArea.setBackground(VERYLIGHTBLUE);
            creditsBottomArea.setOpaque(true);
            creditsBottomPanel.add(creditsBottomArea, BorderLayout.CENTER);

            return creditsBottomPanel;
        }
    }
    
    // Implements listener for Timer to get elapsed time in HH:MM:SS
    public class TimerHandler implements ActionListener
    {
        private int seconds = 0; // Seconds passed
        private int minutes = 0; // minutes passed
        private int hours = 0; // hours passed	
        private String seconds_string = ""; // Formatting how many seconds passed.
        private String minutes_string = ""; // Formatting how many minutes passed.
        private String hours_string = ""; // Formatting how many hours passed.

        public void actionPerformed(ActionEvent evt)
        {
            elapsedTime = elapsedTime + 1000;
            seconds = (elapsedTime / 1000) % 60;
            minutes = (elapsedTime / 60000) % 60;
            hours = (elapsedTime / 3600000);
            seconds_string = String.format("%02d", seconds);
            minutes_string = String.format("%02d", minutes);
            hours_string = String.format("%02d", hours);
            // store time elapsed as a string every second
            playerTimeText = "  [" + hours_string + ":" + minutes_string +
                ":" + seconds_string + "]";
            playerWelcomeLabel.setText(playerName + "'s GeoMazer" + playerTimeText);
        }
    } // end TimerHandler

	// Reusable utility functions across classes.
    // Reads image from the input file
    public Image getMyImage(String pictName)
	{
		// try-catch method for when grabbing images.
		Image picture = null;
		File pictFile = new File(pictName);
		try
		{
			picture = ImageIO.read(pictFile);
		}
		catch (IOException evt)
		{
			System.err.println("\n\n" + pictName + " can't be found.\n\n");
			evt.printStackTrace();
		}
		return picture;
	}
        
    // Used to easier create a button.
    public JButton createButton(String text, Color bgcolor, Color bdcolor, Font font)
    {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgcolor);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setFocusPainted(true);
        button.setBorder(BorderFactory.createLineBorder(bdcolor, 5));

        return button;
    }
    
    // Used when writing to the text area in the leaderbaord. Either shows leaderboard
    // for beginner or advanced players depending on what the player chooses.
    public String getLeaderFile()
    {
        String leaderFile = "";
        if(isBeginner)
            leaderFile = "Leaderboard_easy.txt";
        else if(isAdvanced)
            leaderFile = "Leaderboard_hard.txt";
        return leaderFile;
    }
} // end of GeoMazer class
