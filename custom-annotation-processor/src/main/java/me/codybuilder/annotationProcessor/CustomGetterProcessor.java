package me.codybuilder.annotationProcessor;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

@SupportedAnnotationTypes({"me.codybuilder.annotationProcessor.CustomGetter"})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class CustomGetterProcessor extends AbstractProcessor {

  private Filer filer;
  private Messager messager;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.filer = processingEnv.getFiler();
    this.messager = processingEnv.getMessager();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (TypeElement annotation : annotations) {
      Set<? extends Element> annotated = roundEnv.getElementsAnnotatedWith(annotation);
      for (Element e : annotated) {
        if (e.getKind() != ElementKind.CLASS) {
          messager.printMessage(Diagnostic.Kind.WARNING, "@CustomGetter는 클래스에만 적용됩니다: " + e);
          continue;
        } else {
          messager.printMessage(Diagnostic.Kind.NOTE, "@CustomGetter 처리 대상 클래스:" + e.getSimpleName());
        }

        TypeElement classElement = (TypeElement) e;
        PackageElement pkg = processingEnv.getElementUtils().getPackageOf(classElement);
        String packageName = pkg.isUnnamed() ? "" : pkg.getQualifiedName().toString();
        String originalClassName = classElement.getSimpleName().toString();
        String aspectName = originalClassName + "_GettersAspect";

        StringBuilder sb = new StringBuilder();
        if (!packageName.isEmpty()) {
          sb.append("package ").append(packageName).append(";\n\n");
        }

        // AspectJ inter-type declaration: generate a privileged aspect that adds getter methods into the target class
        // 'privileged' allows the aspect to access private members of the target class
        sb.append("public privileged aspect ").append(aspectName).append(" {\n\n");

        for (Element enclosed : classElement.getEnclosedElements()) {
          if (enclosed.getKind() == ElementKind.FIELD) {
            VariableElement var = (VariableElement) enclosed;
            // skip static fields
            if (var.getModifiers().contains(javax.lang.model.element.Modifier.STATIC)) continue;

            String fieldName = var.getSimpleName().toString();
            String fieldType = var.asType().toString();
            String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

            // inter-type declaration for the original class (same package)
            sb.append("  public ")
                .append(fieldType)
                .append(" ")
                .append(originalClassName)
                .append(".")
                .append(methodName)
                .append("() {\n")
                .append("    return this.")
                .append(fieldName)
                .append(";\n")
                .append("  }\n\n");
          }
        }

        sb.append("}\n");

        // write .aj file into the generated sources directory so ajc (AspectJ) can pick it up
        try {
          String resourceName = (packageName.isEmpty() ? "" : packageName.replace('.', '/') + "/") + aspectName + ".aj";
          JavaFileObject jfo = (JavaFileObject) filer.createResource(StandardLocation.SOURCE_OUTPUT, "", resourceName, classElement);
          try (Writer writer = jfo.openWriter()) {
            writer.write(sb.toString());
          }
          messager.printMessage(Diagnostic.Kind.NOTE, "Generated AspectJ aspect for " + classElement.getQualifiedName());
        } catch (IOException ex) {
          messager.printMessage(Diagnostic.Kind.ERROR, "Failed to write generated aspect: " + ex.getMessage());
        }
      }
    }

    return true;
  }
}
