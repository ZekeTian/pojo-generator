package com.zeketian.plugin.pojogenerator.ui;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiPackage;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.zeketian.plugin.pojogenerator.utils.IntellijFileUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author zeke
 * @description 主界面的内容
 * @date created in 2022/10/5 23:30
 */
public class MainFrame extends JFrame {

    private Project project;

    private Module module;

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

//    /**
//     * 此空参构造方法用于与下面的 main 方法配合使用。
//     */
//    public MainFrame() {
//
//    }
//
//    /**
//     * 该 main 方法是用来生成 gui designer 的代码。每次修改 form 的 UI 内容后，都需要运行此 main 方法。 <br/>
//     * 在运行该 main 方法前，需要先修改 idea 的如下两个配置项：
//     * <ul>
//     *     <li>
//     *         进入配置界面 “Editor > GUI Designer”，然后将 "Generate GUI into" 配置修改为 "Java source code"。
//     *     </li>
//     *     <li>
//     *         进入配置界面 “Build, Execution, Deployment > Build Tools > Gradle”，
//     *         然后将 "Build and run using" 和 "Run tests using" 配置修改为 "IntelliJ IDEA".
//     *     </li>
//     * </ul>
//     * 在运行完之后，会自动生成初始化方法 $$$setupUI$$$，通过该方法可以初始化界面。生成完之后，即可注释该代码。
//     */
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("MainFrame");
//        frame.setContentPane(new MainFrame().mainPanel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }

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

    private void addOnPackageSelectListener() {
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setMinimumSize(new Dimension(600, 300));
        mainPanel.setPreferredSize(new Dimension(600, 300));
        final JLabel label1 = new JLabel();
        label1.setText("package");
        mainPanel.add(label1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("ddl sql");
        mainPanel.add(label2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(1, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        inputSql = new JTextArea();
        inputSql.setLineWrap(false);
        inputSql.setRows(0);
        scrollPane1.setViewportView(inputSql);
        checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(checkBoxPanel, new GridConstraints(4, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cbMybatisPlus = new JCheckBox();
        cbMybatisPlus.setEnabled(true);
        cbMybatisPlus.setSelected(true);
        cbMybatisPlus.setText("Mybatis Plus");
        checkBoxPanel.add(cbMybatisPlus, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        checkBoxPanel.add(spacer3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        checkBoxPanel.add(spacer4, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        packagePanel = new JPanel();
        packagePanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(packagePanel, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        packageText = new JTextField();
        packageText.setEditable(false);
        packageText.setText("");
        packagePanel.add(packageText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        packageBtn = new JButton();
        packageBtn.setText("select");
        packagePanel.add(packageBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
