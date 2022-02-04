package com.player.controller;

import com.player.classes.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Controller_getStreamURL {
    @FXML
    public Button submitButton2;
    @FXML
    public Button addNewButton2;
    @FXML
    public Button deleteButton;
    @FXML
    Button submitButton;
    @FXML
    Button exitButton;
    @FXML
    Button addNewButton;
    @FXML
    TableView<Stream> streamListTable;
    @FXML
    TableColumn<Stream, String> nameAttribute;
    @FXML
    TableColumn<Stream, String> URLAttribute;
    @FXML
    MenuItem openNewStreamFileItem;
    @FXML
    MenuItem saveThisStreamFileItem;
    @FXML
    MenuItem clearListItem;
    @FXML
    MenuItem exitItem;

    ObservableList<Stream> data = FXCollections.observableArrayList();
    Stage stage;
    int chosenIndex;

    public void setCtr(Controller_mainPanel ctr) { //从Controller_mainPanel传入
        this.ctr = ctr;
    }

    Controller_mainPanel ctr;
    Stream chosenStream;

    //初始化列表界面
    @FXML
    private void initialize() {
        nameAttribute.setCellValueFactory(cellData -> cellData.getValue().streamNameProperty()); //显示名称栏
        URLAttribute.setCellValueFactory(cellData -> cellData.getValue().streamURLProperty()); //显示URL栏
        streamListTable.setRowFactory(param -> new TableRowControl()); //建立表格
    }

    //读取.sf文件
    public void openNewStreamFile() throws IOException {
        //读取文件路径
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择流文件");
        stage = (Stage) addNewButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        String pathString = file.toString();

        //判断是否以.sf结尾
        if (!pathString.endsWith(".sf")) {
            //不以.sf结尾，则弹出错误界面
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("notStreamFile.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setTitle("读取列表文件");
            newStage.setScene(new Scene(root, 365, 154));
            newStage.show();
        }
        else {
            data.clear(); //先清空原列表
            InputStreamReader in = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(in);
            String temp = br.readLine(); //读入新列表
            while (temp != null) {
                String[] split1 = temp.split(",");
                data.add(new Stream(split1[0], split1[1])); //分割并保存数据
                temp = br.readLine(); //继续读取
            }
            streamListTable.setItems(data); //显示数据
            in.close();
            br.close();
        }
    }

    //关闭该窗口
    public void exit() {
        stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    //保存.sf文件，通过打开新窗口完成
    public void saveThisFile() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("chooseFolderAndFile.fxml"));
        Parent root = fxmlLoader.load();
        Controller_chooseLocation controller = fxmlLoader.getController();
        controller.setCtr(this);
        Stage newStage = new Stage();
        newStage.setTitle("保存列表文件");
        newStage.setScene(new Scene(root, 530, 278));
        newStage.show();
    }

    //写入.sf文件
    public void writeIntoFile(File toWrite) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toWrite, false), StandardCharsets.UTF_8));//false:不以追加方式写入
        for (Stream stream : data) {
            out.write(stream.getStreamName() + "," + stream.getStreamURL() + "\n"); //使用逗号分开两个数据
        } //使用换行分割数据
        out.close();
    }

    //确认是否清空列表，通过调用新窗口实现
    public void clearList() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ReallyClear.fxml"));
        Parent root = fxmlLoader.load();
        Controller_reallyClear controller = fxmlLoader.getController();
        controller.setCtr(this);
        Stage newStage = new Stage();
        newStage.setTitle("确认");
        newStage.setScene(new Scene(root, 450, 154));
        newStage.show();
    }

    //确认清空列表
    public void doClear() {
        data.clear();
        streamListTable.setItems(data);
    }

    //播放一个流
    public void submitURL() {
        if (chosenStream != null) {
            stage = (Stage) submitButton.getScene().getWindow();
            String streamURL = chosenStream.getStreamURL();
            //stage.close();
            ctr.openNewLocal(streamURL); //将流传给播放器
        }
    }

    //播放一个文件
    public void submitPLAY() {
        //播放器识别的文件地址为file:{文件地址}
        if (chosenStream != null) {
            stage = (Stage) submitButton2.getScene().getWindow();
            String url = "file:"; //先将file前缀写到url内
            url += chosenStream.getStreamURL(); //再将文件路径追加
            //stage.close();
            ctr.openNewLocal(url);
        }
    }

    //添加一个流，通过打开新界面实现
    public void addNewURL() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("inputStream.fxml"));
        Parent root = fxmlLoader.load();
        Controller_inputStream controller = fxmlLoader.getController();
        controller.setCtr(this);
        Stage newStage = new Stage();
        newStage.setTitle("输入新流");
        newStage.setScene(new Scene(root, 487, 148));
        newStage.show();
    }

    //将流添加到列表中
    public void addNewUrl(String name, String url) {
        data.add(new Stream(name, url));
        streamListTable.setItems(data);
    }

    //添加一个文件
    public void addNewFile() {
        //获取文件路径放到url中
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择新的视频文件");
        File file = fileChooser.showOpenDialog(stage);
        String url = file.toString();
        //将文件名作为name
        String fileName = url.substring(url.lastIndexOf("/") + 1); //取到带后缀的文件名，如test.mp4
        String name = fileName.substring(0, fileName.lastIndexOf(".")); //去掉后缀，如test
        data.add(new Stream(name, url)); //添加到列表中
        streamListTable.setItems(data);
    }

    //删除列表
    public void delete() {
        data.remove(chosenStream);
        streamListTable.setItems(data);
    }

    //关闭窗口
    public void exitNow() {
        stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    //设置列表选中
    class TableRowControl extends TableRow<Stream> {
        public TableRowControl() {
            super();
            //当鼠标点击时
            this.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)
                        && event.getClickCount() == 1 //按下鼠标
                        && TableRowControl.this.getIndex() < streamListTable.getItems().size()) { //点在了选项上
                    chosenStream = TableRowControl.this.getItem(); //将相关数据提取出来
                    chosenIndex =TableRowControl.this.getIndex();
                }
            });
        }
    }
}
