package com.zeketian.plugin.pojogenerator.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;

import java.util.List;

/**
 * @author zeke
 * @description intelij 文件工具类
 * @date 2023/11/5 11:56
 */
public class IntellijFileUtil {

    /**
     * 获取 java source 根目录。
     *
     * @param module   当前 module
     * @param rootType 根目录类型，包含 {@link JavaSourceRootType} {@link JavaResourceRootType} 两大类，每大类中又分为测试和非测试两小类。
     * @return 根目录路径
     */
    public static String getJavaSourceRoots(@NotNull Module module, @NotNull JpsModuleSourceRootType<?> rootType) {
        List<VirtualFile> roots = ModuleRootManager.getInstance(module).getSourceRoots(rootType);
        if (roots.isEmpty()) {
            return "";
        }
        return roots.get(0).getPath();
    }


    /**
     * 获取当前打开的文件
     */
    @Nullable
    public static Document getCurrentDocument(Project project) {
        Editor selectedTextEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (selectedTextEditor == null) {
            return null;
        }
        return selectedTextEditor.getDocument();
    }

    /**
     * 获取 psi java 文件
     *
     * @return 如果 document 是 java 文件类型，则返回其对应的 PsiJavaFile；否则，返回 null。
     */
    public static PsiJavaFile getPsiJavaFile(@NotNull Document document, @NotNull Project project) {
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        if (psiFile == null) {
            return null;
        }
        if (psiFile instanceof PsiJavaFile) {
            return (PsiJavaFile) psiFile;
        }
        return null;
    }
}
