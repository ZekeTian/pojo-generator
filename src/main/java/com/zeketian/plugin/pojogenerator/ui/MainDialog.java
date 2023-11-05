package com.zeketian.plugin.pojogenerator.ui;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.zeketian.plugin.pojogenerator.controller.MainController;
import com.zeketian.plugin.pojogenerator.utils.IntellijFileUtil;
import com.zeketian.plugin.pojogenerator.utils.Validator;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author zeke
 * @description 主对话框，用于接收用户输入的内容
 * @date created in 2022/10/5 16:01
 */
public class MainDialog extends DialogWrapper {

    private MainController mainController;
    private MainFrame mainFrame;
    private Project project;
    private Module module;

    public MainDialog(Project project, Module module) {
        super(project);
        this.mainController = new MainController();
        this.project = project;
        this.module = module;
        setTitle("Pojo Generator");
        setOKButtonText("OK");
        setCancelButtonText("Cancel");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        mainFrame = new MainFrame(module);
        return mainFrame.getMainPanel();
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        CustomOkAction customOkAction = new CustomOkAction();
        customOkAction.putValue(Action.DEFAULT, true);
        return new Action[]{customOkAction, this.getCancelAction()};
    }

    protected class CustomOkAction extends OkAction {

        @Override
        protected void doAction(ActionEvent actionEvent) {
            if (mainFrame == null) {
                return;
            }
            String sourceRoots = IntellijFileUtil.getJavaSourceRoots(module, JavaSourceRootType.SOURCE);
            if (StringUtils.isEmpty(sourceRoots)) {
                Messages.showErrorDialog("Could not find java source roots!", "Error");
                return;
            }

            String packageName = mainFrame.getPackageName();
            String inputSql = mainFrame.getInputSql();
            Boolean enableMybatisPlus = mainFrame.isEnableMybatisPlus();

            if (StringUtils.isEmpty(packageName)) {
                Messages.showInfoMessage("Please input package name!", "Info");
                return;
            }
            if (!Validator.isPackage(packageName)) {
                Messages.showInfoMessage("Please input a valid package name.", "Info");
                return;
            }
            if (StringUtils.isEmpty(inputSql)) {
                Messages.showInfoMessage("Please input sql!", "Info");
                return;
            }

            try {
                mainController.generatePojo(sourceRoots, inputSql, packageName, enableMybatisPlus);
                super.doAction(actionEvent);
            } catch (Exception e) {
                Messages.showErrorDialog("Failed to generate.\n" + e.getMessage(), "Error");
            }
        }
    }
}
