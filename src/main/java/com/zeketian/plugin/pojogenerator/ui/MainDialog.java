package com.zeketian.plugin.pojogenerator.ui;

import java.awt.event.ActionEvent;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.zeketian.plugin.pojogenerator.controller.MainController;

/**
 * @author zeke
 * @description 主对话框，用于接收用户输入的内容
 * @date created in 2022/10/5 16:01
 */
public class MainDialog extends DialogWrapper {

    private MainController mainController;
    private MainFrame mainFrame;
    private Project project;

    public MainDialog(Project project) {
        super(project);
        this.mainController = new MainController();
        this.project = project;
        setTitle("Pojo Generator");
        setOKButtonText("OK");
        setCancelButtonText("Cancel");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        mainFrame = new MainFrame();
        return mainFrame.getMainPanel();
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        CustomOkAction customOkAction = new CustomOkAction();
        customOkAction.putValue(Action.DEFAULT, true);
        return new Action[] {customOkAction, this.getCancelAction(), getHelpAction()};
    }

    protected class CustomOkAction extends OkAction {

        @Override
        protected void doAction(ActionEvent actionEvent) {
            if (mainFrame == null) {
                return;
            }
            String packageName = mainFrame.getInputPackage().getText();
            String inputSql = mainFrame.getInputSql().getText();
            try {
                mainController.generatePojo(project.getBasePath(), inputSql, packageName, false);
            } catch (Exception e) {
                Messages.showErrorDialog("Failed to generate", "Error");
            }
            super.doAction(actionEvent);
        }
    }
}
