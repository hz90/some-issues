package zpr.zpr;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;

public class JavaParser {
	private static final String path1 = "C:\\pleiades\\workspace\\zpr\\src\\main\\java\\zpr\\zpr\\App.java";
	private static final String path2 = "C:/pleiades/workspace/autotool/src/main/java/com/auto/autotool/App.java";
	private static final String path = "C:\\pleiades\\workspace\\spa_admin_api\\src\\main\\java\\com\\spa\\pro\\service\\common\\youtube\\YoutubeResourceService.java";
	private static final String path3 = "C:\\pleiades\\workspace\\spa_admin_api\\target\\classes\\com\\spa\\pro\\common\\annotation\\Decrypt.class";

	private JavacFileManager fileManager;
	private JavacTool javacTool;

	public JavaParser() {
		Context context = new Context();
		fileManager = new JavacFileManager(context, true, Charset.defaultCharset());
		javacTool = JavacTool.create();
	}

	public void parseJavaFiles() {
		Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(path);
		JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files);
		JavacTask javacTask = (JavacTask) compilationTask;
		try {
			Iterable<? extends CompilationUnitTree> result = javacTask.parse();
			for (CompilationUnitTree tree : result) {
				tree.accept(new SourceVisitor(), null);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class SourceVisitor extends TreeScanner<Void, Void> {

		private String currentPackageName = null;

		@Override
		public Void visitCompilationUnit(CompilationUnitTree node, Void aVoid) {
			return super.visitCompilationUnit(node, aVoid);
		}

		@Override
		public Void visitVariable(VariableTree node, Void aVoid) {
			formatPtrln("variable name: %s, type: %s, kind: %s, package: %s", node.getName(), node.getType(),
					node.getKind(), currentPackageName);
			return null;
		}

		@Override
		public Void visitClass(ClassTree node, Void aVoid) {
			formatPtrln("class name: %s", node.getSimpleName());
			for (Tree member : node.getMembers()) {
				if (member instanceof VariableTree) {
					VariableTree variable = (VariableTree) member;
					List<? extends AnnotationTree> annotations = variable.getModifiers().getAnnotations();
					if (annotations.size() > 0) {
						formatPtrln("variable: %s, annotaion: %s", variable.getName(),
								annotations.get(0).getAnnotationType());
					} else {
						formatPtrln("variable: %s", variable.getName());
					}
				}
			}
			return super.visitClass(node, aVoid);
		}
		
		@Override
		public Void visitMethod(MethodTree paramMethodTree, Void paramP) {
			formatPtrln("method--------------------: %s", paramMethodTree.getName());
			return super.visitMethod(paramMethodTree, paramP);
		}
		@Override
		public Void visitMethodInvocation(MethodInvocationTree paramMethodInvocationTree, Void paramP) {
			formatPtrln("methodInvocation: %s", paramMethodInvocationTree);
			return super.visitMethodInvocation(paramMethodInvocationTree, paramP);
		}
	}

	public static void formatPtrln(String format, Object... args) {
		System.out.println(String.format(format, args));
	}

	public static void main(String[] args) {

		new JavaParser().parseJavaFiles();
	}
}
