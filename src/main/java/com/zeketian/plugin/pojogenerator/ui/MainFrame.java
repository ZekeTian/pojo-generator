package com.zeketian.plugin.pojogenerator.ui;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiPackage;
import com.zeketian.plugin.pojogenerator.utils.IntellijFileUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

/**
 * @author zeke
 * @description 主界面的内容
 * @date created in 2022/10/5 23:30
 */
public class MainFrame extends JFrame {

    private final Project project;

    private final Module module;

    private JPanel mainPanel;

    private JTextArea inputSql;

    private JPanel checkBoxPanel;

    private JCheckBox cbMybatisPlus;
    private JPanel packagePanel;
    private JTextField packageText;
    private JButton packageBtn;


    public MainFrame(Module module) {
        setSize(600, 300);
        this.module = module;
        this.project = module.getProject();
        packageBtn.addActionListener(l -> {
            addOnPackageSelectListener();
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public String getPackageName() {
        return packageText.getText().trim();
    }

    public String getInputSql() {
        return inputSql.getText().trim();
    }

    public Boolean isEnableMybatisPlus() {
        return cbMybatisPlus.isSelected();
    }

    public void addOnPackageSelectListener() {
        // 创建一个包选择器对话框
        PackageChooserDialog chooserDialog = new PackageChooserDialog("package chooser", module);

        // 设置默认的 package
        String packageName = getPackageOfCurrentJavaFile();
        if (!StringUtils.isEmpty(packageName)) {
            chooserDialog.selectPackage(packageName);
        }

        if (chooserDialog.showAndGet()) {
            PsiPackage selectedPackage = chooserDialog.getSelectedPackage();
            packageText.setText(selectedPackage.getQualifiedName());
        }
    }

    /**
     * 获取当前打开的文件所在的 package。
     *
     * @return 如果当前打开的文件是 java 文件，则返回此 java 文件所在的 package；如果不是 java 文件，则返回空串；
     */
    private String getPackageOfCurrentJavaFile() {
        Document document = IntellijFileUtil.getCurrentDocument(project);
        if (document == null) {
            return "";
        }

        PsiJavaFile psiJavaFile = IntellijFileUtil.getPsiJavaFile(document, project);
        if (psiJavaFile == null) {
            // 当前打开的文件不是 java 文件，则直接返回空串
            return "";
        }

        return psiJavaFile.getPackageName();
    }

}
