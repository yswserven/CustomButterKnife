package com.custom.compiler;

import com.custom.annotation.BindView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class AnnotationCompile extends AbstractProcessor {

    private Filer filer;

    /* 获取生成文件对象，还可以获取别的东西 @author Ysw created 2020/2/14 */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    /* 确定当前APT处理所有模块中的哪些注解 @author Ysw created 2020/2/14 */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    /* 确定支持的JDK版本 @author Ysw created 2020/2/14 */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        /* 对含有此注解的成员变量根据Activity进行分类 @author Ysw created 2020/2/14 */
        Map<String, List<VariableElement>> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            VariableElement variableElement = (VariableElement) element;
            String activityName = variableElement.getEnclosingElement().getSimpleName().toString();
            List<VariableElement> elementList = map.get(activityName);
            if (elementList == null) {
                elementList = new ArrayList<>();
                map.put(activityName, elementList);
            }
            elementList.add(variableElement);
        }

        /* 对不同的Activity进行文件生成 @author Ysw created 2020/2/14 */
        if (map.size() > 0) {
            Writer writer = null;
            for (String activityName : map.keySet()) {
                List<VariableElement> elementList = map.get(activityName);
                TypeElement enclosingElement = (TypeElement) elementList.get(0).getEnclosingElement();
                String packageName = processingEnv.getElementUtils().getPackageOf(enclosingElement).toString();
                try {
                    JavaFileObject classFile = filer.createSourceFile(packageName + "." + activityName + "_ViewBinding");
                    writer = classFile.openWriter();
                    writer.write("package " + packageName + ";\n");
                    writer.write("import com.custom.butterknife.core.IBinder;\n");
                    writer.write("public class " + activityName + "_ViewBinding implements IBinder<"
                            + packageName + "." + activityName + ">{\n");
                    writer.write("public void bind(" + packageName + "." + activityName + " target){\n");
                    for (VariableElement element : elementList) {
                        String variableName = element.getSimpleName().toString();
                        int id = element.getAnnotation(BindView.class).value();
                        TypeMirror typeMirror = element.asType();
                        writer.write("target." + variableName + "=(" + typeMirror + ")target.findViewById(" + id + ");\n");
                    }
                    writer.write("}}");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }
}
