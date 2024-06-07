package com.example.lesothotriviagame;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.Objects;

public class HelloApplication extends Application {

    private Question[] questions = {
            new Question("What is the highest mountain peak in Lesotho?",
                    0,
                    "/Images/Lesotho.png",
                    new Option[]{
                            new Option("Thabana Ntlenyana", true, null),
                            new Option("Bokong", false, null),
                            new Option("Mont-aux-Sources", false, null),
                            new Option("Laing's Nek", false, null)
                    }),
            new Question("What is the capital of Lesotho?",
                    2,
                    "/Images/Maseru.jpg",
                    new Option[]{
                            new Option("Mokhotlong", false, null),
                            new Option("Leribe", false, null),
                            new Option("Maseru", true, null),
                            new Option("Pretoria", false, null)
                    }),
            new Question("Which flag represents Lesotho?",
                    0,
                    null,
                    new Option[]{
                            new Option("Option A",  true, "/video/F.mp4"),
                            new Option("Option B",  false, "/video/N.mp4"),
                            new Option("Option C",  false, "/video/C.mp4"),
                            new Option("Option D",  false, "/video/W.mp4")
                    }),
            new Question("When does Lesotho celebrate independence day?",
                    1,
                    "/Images/ID.jpg",
                    new Option[]{
                            new Option("2 July", false, null),
                            new Option("4 October", true, null),
                            new Option("17 April", false, null),
                            new Option("23 May", false, null)
                    }),
            new Question("Which of these four cities is the most northern town in Lesotho?",
                    0,
                    "/Images/R.jpg",
                    new Option[]{
                            new Option("Leribe", true, null),
                            new Option("Teyateyaneng", false, null),
                            new Option("Mokhotlong", false, null),
                            new Option("Thaba-Tseka", false, null)
                    }),
            new Question("What is the highest falls in Lesotho?",
                    1,
                    "/Images/Ma.jpg",
                    new Option[]{
                            new Option("Ribaneng falls", false, null),
                            new Option("Maletsunyane falls", true, null),
                            new Option("Ketane falls", false, null),
                            new Option("Lisbon falls", false, null)
                    }),
    };

    private int currentQuestionIndex = 0;
    private int score = 0;

    private Label questionLabel;
    private ProgressBar progressBar;
    private VBox optionsBox;
    private Rectangle borderBox;
    private Label feedbackLabel;
    private AudioClip backgroundAudio;
    private Label clickLabel; // Label for clicking the correct image

    @Override
    public void start(Stage primaryStage) {
        questionLabel = new Label();
        progressBar = new ProgressBar(0);
        optionsBox = new VBox(10);
        borderBox = new Rectangle(350, 250);
        feedbackLabel = new Label();

        // Set the style classes for the UI elements
        questionLabel.getStyleClass().add("question-label");
        optionsBox.getStyleClass().add("options-box");
        feedbackLabel.getStyleClass().add("feedback-label");

        borderBox.setStroke(Color.BLUE);
        borderBox.setStrokeWidth(2);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10, 0, 0, 0)); // 10 pixel padding to the top
        root.getChildren().addAll(createHeader(), questionLabel, progressBar, optionsBox, feedbackLabel);

        Scene scene = new Scene(root, 500, 500); // Increased scene size
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.show();

        // Play background audio
        backgroundAudio = new AudioClip(getClass().getResource("/audio/Lesotho.m4a").toExternalForm());
        backgroundAudio.setCycleCount(AudioClip.INDEFINITE); // Set audio to loop indefinitely
        backgroundAudio.play(); // Start playing the background audio

        // Instruction label for all questions
        clickLabel = new Label("Enjoy the most interesting Lesotho trivia Questions");
        clickLabel.getStyleClass().add("instruction-label");
        clickLabel.setTextFill(Color.BLUE);
        clickLabel.setAlignment(Pos.CENTER); // Center align the label
        clickLabel.setPadding(new Insets(10, 0, 0, 0)); // Add padding to position it below the question
        root.getChildren().add(clickLabel);

        displayQuestion(root);
    }

    private StackPane createHeader() {
        StackPane header = new StackPane();
        header.setStyle("-fx-background-color: green;");
        Label appName = new Label("Lesotho Trivia Game");
        appName.setStyle("-fx-font-size: 24px;");
        header.getChildren().add(appName);
        return header;
    }

    private void displayQuestion(VBox root) {
        Question currentQuestion = questions[currentQuestionIndex];
        questionLabel.setText(currentQuestion.getQuestion());
        progressBar.setProgress((double) (currentQuestionIndex + 1) / questions.length);
        feedbackLabel.setText("");

        optionsBox.getChildren().clear(); // Clear previous content

        Option[] options = currentQuestion.getOptions();

        if (currentQuestionIndex == 2) { // For question 3
            Button playButton = new Button("Play All Videos");
            playButton.setOnAction(event -> {
                playAllVideos(options);
            });
            optionsBox.getChildren().add(playButton);
        }

        // Add the image below the question
        if (currentQuestion.getImagePath() != null && !currentQuestion.getImagePath().isEmpty()) {
            try {
                InputStream inputStream = getClass().getResourceAsStream(currentQuestion.getImagePath());
                if (inputStream != null) {
                    ImageView imageView = new ImageView(new Image(inputStream));
                    imageView.setFitWidth(150);
                    imageView.setPreserveRatio(true);
                    optionsBox.setAlignment(Pos.CENTER); // Center align the optionsBox
                    optionsBox.getChildren().add(imageView);
                } else {
                    System.out.println("Failed to load image: " + currentQuestion.getImagePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HBox optionsRow = new HBox(10);
        optionsRow.setAlignment(Pos.CENTER);

        for (Option option : options) {
            VBox optionBox = new VBox();
            Label descriptionLabel = new Label(option.getDescription());
            descriptionLabel.setAlignment(Pos.CENTER);
            optionBox.getChildren().add(descriptionLabel);
            HBox.setHgrow(optionBox, Priority.ALWAYS); // Allow option box to grow horizontally
            Button optionButton = new Button();
            optionButton.getStyleClass().add("option-button");
            optionButton.setGraphic(optionBox);
            optionButton.setOnAction(event -> {
                // Check the answer
                checkAnswer(option);
            });
            optionsRow.getChildren().add(optionButton);
        }

        // Add the options row to the options box
        optionsBox.getChildren().add(optionsRow);
    }

    private void playAllVideos(Option[] options) {
        optionsBox.getChildren().clear(); // Clear existing content

        // Create an HBox to hold all video-option pairs horizontally
        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER);

        // Iterate through each option to display videos and corresponding option buttons
        for (Option option : options) {
            if (option.getVideoPath() != null && !option.getVideoPath().isEmpty()) {
                Media media = new Media(getClass().getResource(option.getVideoPath()).toExternalForm());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setFitWidth(200);  // Set fixed width
                mediaView.setFitHeight(100); // Set fixed height

                Button playButton = new Button("Play");
                playButton.setOnAction(event -> mediaPlayer.play());

                Button stopButton = new Button("Stop");
                stopButton.setOnAction(event -> mediaPlayer.stop());

                VBox videoControls = new VBox(10, playButton, stopButton);
                videoControls.setAlignment(Pos.CENTER);

                VBox videoContainer = new VBox(10, mediaView, videoControls);
                videoContainer.setAlignment(Pos.CENTER);

                // Create the option button for this video
                VBox optionBox = new VBox();
                Label descriptionLabel = new Label(option.getDescription());
                descriptionLabel.setAlignment(Pos.CENTER);
                optionBox.getChildren().add(descriptionLabel);
                Button optionButton = new Button();
                optionButton.getStyleClass().add("option-button");
                optionButton.setGraphic(optionBox);
                optionButton.setOnAction(event -> {
                    // Check the answer
                    checkAnswer(option);
                });

                // Create an HBox to hold the video and option button horizontally
                HBox videoOptionPair = new HBox(20);
                videoOptionPair.setAlignment(Pos.CENTER);
                videoOptionPair.getChildren().addAll(videoContainer, optionButton);

                // Add the video-option pair to the main HBox
                hbox.getChildren().add(videoOptionPair);
            }
        }

        optionsBox.getChildren().add(hbox);
    }

    private void checkAnswer(Option selectedOption) {
        Question currentQuestion = questions[currentQuestionIndex];
        boolean correct = selectedOption.isCorrect();
        if (correct) {
            score++;
            animateFeedback("Correct!", Color.GREEN, "/audio/Correct.m4a");
        } else {
            // If the answer is incorrect, display the correct answer
            animateFeedback("Incorrect! The correct answer is: " + currentQuestion.getCorrectOption().getDescription(), Color.RED, "/audio/Wrong.m4a");
        }

        animateBorderBox();

        // Transition to next question after a delay
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.length) {
                displayQuestion(optionsBox);
            } else {
                displayScoreScreen();
            }
        }));
        timeline.play();
    }

    private void animateFeedback(String message, Color color, String audioPath) {
        feedbackLabel.setText(message);
        feedbackLabel.setTextFill(color);
        AudioClip audioClip = new AudioClip(getClass().getResource(audioPath).toExternalForm());
        audioClip.play();
    }

    private void animateBorderBox() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(borderBox.strokeWidthProperty(), 2)),
                new KeyFrame(Duration.seconds(5), new KeyValue(borderBox.strokeWidthProperty(), 10))
        );
        timeline.play();
    }

    private void displayScoreScreen() {
        optionsBox.getChildren().clear(); // Clear existing content

        Label scoreLabel = new Label("Your Score: " + score + "/" + questions.length);
        scoreLabel.getStyleClass().add("feedback-label");

        VBox scoreContainer = new VBox(scoreLabel);
        scoreContainer.setAlignment(Pos.CENTER);

        Button replayButton = new Button("Replay");
        replayButton.setOnAction(event -> {
            currentQuestionIndex = 0;
            score = 0;
            displayQuestion(optionsBox);
        });

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(event -> System.exit(0));

        HBox buttonBox = new HBox(20, replayButton, quitButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, scoreContainer, buttonBox);
        root.setAlignment(Pos.CENTER);

        optionsBox.getChildren().add(root);

        if (score >= questions.length / 2) {
            animateWinning();
        } else {
            animateLosing();
        }
    }

    private void animateWinning() {
        feedbackLabel.setText("You Won!");
        feedbackLabel.setTextFill(Color.GREEN);
        AudioClip winSound = new AudioClip(getClass().getResource("/audio/Winning.m4a").toExternalForm());
        winSound.play();
    }

    private void animateLosing() {
        feedbackLabel.setText("You can make it next time.");
        feedbackLabel.setTextFill(Color.RED);
        AudioClip loseSound = new AudioClip(getClass().getResource("/audio/Game.m4a").toExternalForm());
        loseSound.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class Question {
        private String question;
        private int correctOptionIndex;
        private String imagePath;
        private Option[] options;

        public Question(String question, int correctOptionIndex, String imagePath, Option[] options) {
            this.question = question;
            this.correctOptionIndex = correctOptionIndex;
            this.imagePath = imagePath;
            this.options = options;
        }

        public String getQuestion() {
            return question;
        }

        public Option[] getOptions() {
            return options;
        }

        public Option getCorrectOption() {
            return options[correctOptionIndex];
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    public class Option {
        private String description;
        private boolean correct;
        private String videoPath;

        public Option(String description, boolean correct, String videoPath) {
            this.description = description;
            this.correct = correct;
            this.videoPath = videoPath;
        }

        public String getDescription() {
            return description;
        }

        public boolean isCorrect() {
            return correct;
        }

        public String getVideoPath() {
            return videoPath;
        }
    }
}
