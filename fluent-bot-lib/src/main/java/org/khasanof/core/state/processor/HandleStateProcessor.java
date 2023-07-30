//package org.khasanof.core.state.processor;
//
//import lombok.SneakyThrows;
//import org.khasanof.core.config.FluentConfig;
//import org.khasanof.core.state.SimpleStateService;
//
//import javax.annotation.processing.*;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.TypeElement;
//import javax.tools.JavaCompiler;
//import javax.tools.JavaFileObject;
//import javax.tools.ToolProvider;
//import java.io.PrintWriter;
//import java.util.Set;
//
///**
// * @author Nurislom
// * @see org.khasanof.core.state.processor
// * @since 23.07.2023 23:20
// */
//public class HandleStateProcessor extends AbstractProcessor {
//
//    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//
//    private final FluentConfig config = FluentConfig.getInstance();
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//    }
//
//    @SneakyThrows
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        final Class<? extends Enum> enumType = SimpleStateService.getInstance().getType();
//        String NAME = "HandleState";
//        JavaFileObject classFile = processingEnv.getFiler().createClassFile(NAME.concat(".java"));
//        try (PrintWriter writer = new PrintWriter(classFile.openWriter())) {
//            printWriter(enumType, writer);
//        }
//        return true;
//    }
//
//    private void printWriter(Class<? extends Enum> enumType, PrintWriter writer) {
//        writer.print("package ");
//        writer.print(config.getConfig().getProjectGroupId());
//        writer.println(";");
//        writer.println();
//        writer.println("@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})");
//        writer.println("@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)");
//        writer.println("public @interface HandleState {");
//        writer.println();
//        writer.println("    ".concat(enumType.getSimpleName()).concat(" value();"));
//        writer.println();
//        writer.println("    boolean proceedHandleMethods() default false;");
//        writer.println();
//        writer.println("}");
//    }
//}
