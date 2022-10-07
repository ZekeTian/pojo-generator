package com.zeketian.plugin.pojogenerator.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.zeketian.plugin.pojogenerator.ui.MainDialog;

/**
 * @author zeke
 * @description 在 Generate 菜单中注册的自定义 action
 * @date created in 2022/10/5 16:16
 */
public class PojoGenerator extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (project == null) {
            Messages.showInfoMessage("There is no project is opened", "Alert");
            return;
        }

        MainDialog mainDialog = new MainDialog(project);
        mainDialog.show();

        // MainController mainController = new MainController();
        //
        // String basePath = System.getProperty("user.dir");
        //
        // try {
        // String sql = FileUtils.readFileToString(new
        // File("E:\\01-code\\01-java-code\\idea\\02-my-project\\01-pojo-generator\\src\\main\\resources\\sql\\test.sql"),
        // "UTF-8");
        // Messages.showInfoMessage(sql, "sql");
        // } catch (IOException ex) {
        // ex.printStackTrace();
        // }
    }
}
