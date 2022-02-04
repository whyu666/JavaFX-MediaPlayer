package com.player.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Controller_mainPanel {
    //从fxml文件中读入控件
    @FXML
    MediaView playView;
    @FXML
    Button playBT;
    @FXML
    Button stopBT;
    @FXML
    Button maxBT;
    @FXML
    Button volumeBT;
    @FXML
    Slider volumeSD;
    @FXML
    BorderPane controlBorderPane;
    @FXML
    AnchorPane mainPanel;
    @FXML
    MenuBar menu;
    @FXML
    ImageView imageView;
    @FXML
    Slider processSD;
    @FXML
    Label timeLB;

    //声明变量
    private MediaPlayer mediaPlayer;
    private Media media;
    private String playIcon;
    private String pauseIcon;
    private String stopIcon;
    private String volOffIcon;
    private String volOnIcon;
    private String maxIcon;
    private String backImage;
    private Duration duration;
    Boolean isMute = false;
    double currentWidth, currentHeight;
    Scene scene;
    String url = null;
    Stage stage;
    Boolean isFullWindow = false;
    Boolean isPlaying = false;
    Integer iconHeight = 23;
    Integer iconWidth = 20;

    //初始化播放器界面
    public void init() {
        //从resources读取图标资源
        ClassLoader classLoader = Controller_getStreamURL.class.getClassLoader();
        playIcon = Objects.requireNonNull(classLoader.getResource("icon/play.png")).toString(); //播放图标
        pauseIcon = Objects.requireNonNull(classLoader.getResource("icon/pause.png")).toString(); //暂停图标
        stopIcon = Objects.requireNonNull(classLoader.getResource("icon/stop.png")).toString(); //停止图标
        volOffIcon = Objects.requireNonNull(classLoader.getResource("icon/volume_off.png")).toString(); //静音图标
        volOnIcon = Objects.requireNonNull(classLoader.getResource("icon/volume_on.png")).toString(); //开启声音图标
        maxIcon = Objects.requireNonNull(classLoader.getResource("icon/max.png")).toString(); //全屏图标
        backImage = Objects.requireNonNull(classLoader.getResource("icon/background.png")).toString(); //无文件时播放图标

        //设置各控件图标
        setIcon(playBT, playIcon);
        setIcon(stopBT, stopIcon);
        setIcon(volumeBT, volOnIcon);
        setIcon(maxBT, maxIcon);

        //初始化整个界面
        scene = mainPanel.getScene();
        stage = (Stage) mainPanel.getScene().getWindow();

        //实现窗口大小改变时，视频等比例缩放
        scene.widthProperty().addListener((observable, oldValue, newValue) -> { //宽度变化
            currentWidth = newValue.intValue();
            adjustSize();
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> { //高度变化
            currentHeight = newValue.intValue();
            adjustSize();
        });

        //显示初始化播放图标
        Image backGround = new Image(backImage);
        imageView.setImage(backGround);

        //设置初始化图标位置
        imageView.setFitHeight(backGround.getHeight());
        imageView.setFitWidth(backGround.getWidth());

        //初始化整个窗口大小
        currentWidth = mainPanel.getWidth();
        currentHeight = mainPanel.getHeight();
        adjustSize();

        processSD.setVisible(false); //初始化隐藏进度条
    }

    //设置图标
    private void setIcon(Button button, String path) {
        //设置图标大小，并关联按钮
        Image icon = new Image(path);
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(iconWidth); //转换图标长度
        imageView.setFitHeight(iconHeight); //转换图标宽度
        button.setGraphic(imageView); //将button按钮和图标关联在一起

        //当图标点击时，图标变亮
        ColorAdjust colorAdjust = new ColorAdjust();
        button.setOnMousePressed(event -> { //鼠标按下时
            colorAdjust.setBrightness(0.5); //图标变亮
            button.setEffect(colorAdjust);
        });
        button.setOnMouseReleased(event -> { //鼠标释放时
            colorAdjust.setBrightness(0); //图标变暗
            button.setEffect(colorAdjust);
        });
    }

    //播放前，初始化播放界面
    public void initBeforePlay() {
        mediaPlayer.pause(); //默认暂停界面
        mediaPlayer.setVolume(volumeSD.getValue() / 100); //根据音量条设置音量大小
        imageView.setVisible(false); //播放视频时，不显示背景图片

        //当音量条被拖动时，修改音量大小
        volumeSD.valueProperty().addListener((observable, oldValue, newValue) -> mediaPlayer.setVolume(newValue.doubleValue() / 100));

        //当进度条被拖动时，修改播放时间
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> updateTime());

        timeLB.setVisible(false);
        playBT.setDisable(false);
        processSD.setVisible(true);
    }

    //选择本地文件播放
    public void openNewLocal() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择新的视频文件");
        File file = fileChooser.showOpenDialog(stage); //文件选择框
        url = file.toURI().toString(); //读取到文件地址

        //如果有视频正在播放，则先停止当前视频
        if (media != null) {
            mediaPlayer.stop();
            isPlaying = false;
            setIcon(playBT, playIcon);
        }

        media = new Media(url); //先生成Media类对象
        mediaPlayer = new MediaPlayer(media); //设置新视频，用MediaPlayer类对象加载media对象
        playView.setMediaPlayer(mediaPlayer); //将mediaPlayer放在playView中
        initBeforePlay(); //准备播放视频
    }

    //选择播放列表中的视频播放
    public void openNewLocal(String f) {
        if (media != null) {
            mediaPlayer.stop();
            isPlaying = false;
            setIcon(playBT, playIcon);
        }
        media = new Media(f);
        mediaPlayer = new MediaPlayer(media);
        playView.setMediaPlayer(mediaPlayer);
        initBeforePlay();
    }

    //打开播放列表
    public void openStream() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("getStreamURL.fxml"));
        Parent root = fxmlLoader.load();
        Controller_getStreamURL controller = fxmlLoader.getController();
        Stage newStage = new Stage();
        newStage.setTitle("播放列表");
        newStage.setScene(new Scene(root, 640, 480));
        controller.setCtr(this); //将Controller_mainPanel信息传到相应控制器中
        newStage.show();
    }

    //调整播放器窗口大小
    public void adjustSize() {
        //计算并设置相应控件大小
        mainPanel.setPrefSize(currentWidth, currentHeight);
        AnchorPane.setBottomAnchor(controlBorderPane, 0.0);
        AnchorPane.setTopAnchor(menu, 0.0);
        AnchorPane.setTopAnchor(playView, menu.getHeight());
        playView.setFitHeight(currentHeight - menu.getHeight() - controlBorderPane.getHeight());
        playView.setFitWidth(currentWidth);

        //使菜单栏、控件栏保持充满整个窗口
        menu.setPrefWidth(currentWidth);
        controlBorderPane.setPrefWidth(currentWidth);
        AnchorPane.setTopAnchor(imageView, currentHeight / 2 - imageView.getFitHeight() / 2);
        AnchorPane.setLeftAnchor(imageView, currentWidth / 2 - imageView.getFitWidth() / 2);
    }

    //关闭播放器
    public void exit() {
        stage.close();
    }

    //将两个Duration参数转化为hh：mm：ss形式
    private String formatTime(Duration elapsed, Duration duration) {
        //elapsed为视频总长；duration为当前播放时间
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        int elapsedMinutes = (intElapsed - elapsedHours * 60 * 60) / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

        int intDuration = (int) Math.floor(duration.toSeconds());
        int durationHours = intDuration / (60 * 60);
        int durationMinutes = (intDuration - durationHours * 60 * 60) / 60;
        int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

        //按是否超过一小时，分别返回值
        if (durationHours > 0) {
            return String.format("%02d:%02d:%02d / %02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds, durationHours, durationMinutes, durationSeconds);
        }
        else {
            return String.format("%02d:%02d / %02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes, durationSeconds);
        }
    }

    //调节视频时间
    public void updateTime() {
        //先判断各控件状态
        if (processSD != null && timeLB != null && volumeSD != null && volumeBT != null) {
            //Platform.runLater:另外启动线程
            Platform.runLater(() -> {
                //currentTime：当前时间 duration：总时长
                processSD.setVisible(true); //设置进度条可见
                Duration currentTime = mediaPlayer.getCurrentTime();
                timeLB.setText(formatTime(currentTime, duration)); //设置时间框的时间
                processSD.setDisable(duration.isUnknown()); //无法读取时间时，隐藏进度条

                //判断是否修改进度条
                if (!processSD.isDisabled() && duration.greaterThan(Duration.ZERO) && !processSD.isValueChanging()) {
                    processSD.setValue(currentTime.toMillis() / duration.toMillis() * 100); //按时间修改进度条
                }

                //当进度条移动时
                if (processSD.isValueChanging()) {
                    mediaPlayer.seek(duration.multiply(processSD.getValue() / 100.0)); //按进度条修改时间
                }
            });
        }
    }

    //设置全屏
    public void setMaximizeButton() {
        if (!isFullWindow) {
            stage.setFullScreen(true);
            isFullWindow = true;
        }
        else {
            stage.setFullScreen(false);
            isFullWindow = false;
        }
    }


    //停止播放视频
    public void stop() {
        if (media != null) {
            mediaPlayer.stop();
            isPlaying = false;
            setIcon(playBT, playIcon); //替换图标
            timeLB.setVisible(false);
            processSD.setVisible(false);
        }
    }

    //关闭当前视频
    public void stopAction() {
        if (media != null) {
            mediaPlayer.stop();
            playView.setMediaPlayer(null); //清屏
            imageView.setVisible(true); //恢复背景图标
            isPlaying = false;
            setIcon(playBT, playIcon);
            playBT.setDisable(true); //playButton不能使用
            timeLB.setVisible(false);
            processSD.setVisible(false);
        }
    }

    //设置播放&暂停播放
    public void play() {
        //先判断是否成功载入了视频
        if (media != null && playView.getMediaPlayer() != null) {
            //暂停->播放
            if (!isPlaying) {
                mediaPlayer.play();
                duration = mediaPlayer.getMedia().getDuration(); //获取当前时间值赋给duration
                isPlaying = true;
                setIcon(playBT, pauseIcon);
                timeLB.setVisible(true);
                processSD.setVisible(true);
            }

            //播放->暂停
            else {
                mediaPlayer.pause();
                isPlaying = false;
                setIcon(playBT, playIcon);
                timeLB.setVisible(true);
                processSD.setVisible(true);
            }
        }
    }

    //修改声音大小
    public void changeMute() {
        if (!isMute) {
            isMute = true;
            setIcon(volumeBT, volOffIcon); //更换喇叭图标
        }
        else {
            isMute = false;
            setIcon(volumeBT, volOnIcon);
        }

        //判断是否正在播放
        if (media != null) {
            if (!isMute) {
                mediaPlayer.setMute(true);
            }
            else {
                mediaPlayer.setMute(false);
            }
        }
    }

    //加载关于窗口
    public void about() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("about.fxml"));
        Parent root = fxmlLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("关于");
        newStage.setScene(new Scene(root, 550, 250));
        newStage.show();
    }

    //加载设置窗口
    public void setting() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("setting.fxml"));
        Parent root = fxmlLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("设置");
        newStage.setScene(new Scene(root, 300, 120));
        newStage.show();
    }

}
