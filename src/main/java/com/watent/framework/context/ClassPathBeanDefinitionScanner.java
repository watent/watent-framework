package com.watent.framework.context;

import com.watent.framework.bean.BeanDefinitionRegistry;
import com.watent.framework.bean.BeanFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dylan
 */
public class ClassPathBeanDefinitionScanner {

    private Logger logger = LoggerFactory.getLogger(ClassPathBeanDefinitionScanner.class);

    private BeanDefinitionRegistry registry;

    private BeanFactory beanFactory;

    private BeanDefinitionReader reader;

    private PathMatcher pathMatcher = new AntPathMatcher();

    private String resourcePattern = "**/*.class";

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
        this.reader = new AnnotationBeanDefinitionReader(registry);
    }

    public void scan(String... basePackages) throws Throwable {

        if (null == basePackages || basePackages.length <= 0) {
            return;
        }
        for (String bp : basePackages) {
            reader.loadBeanDefinition(doScan(bp));
        }
    }

    private Resource[] doScan(String basePackage) throws Throwable {

        // 扫描包下的类 eg: com.watent
        // 构造初步匹配模式串，= 给入的包串 + / + **/*.class，替换里面的.为/
        String pathPattern = StringUtils.replace(basePackage, ".", "/") + "/" + this.resourcePattern;
        if (pathPattern.charAt(0) != '/') {
            pathPattern = "/" + pathPattern;
        }
        //  /com/watent/**/*.class
        // 找出模式的根包路径
        String rootPath = this.determineRootDir(pathPattern);
        // 得到文件名匹配的绝对路径模式
        String fullPattern = this.getClass().getResource("/").toString() + pathPattern;
        // 根据根包理解得到根包对应的目录
        File rootDir = new File(this.getClass().getResource(rootPath).toString());
        // 存放找到的类文件的resource集合
        Set<Resource> scannedClassFileResources = new HashSet<>();
        // 调用doRetrieveMatchingFiles来扫描class文件
        doRetrieveMatchingFiles(fullPattern, rootDir, scannedClassFileResources);

        return (Resource[]) scannedClassFileResources.toArray();
    }

    protected void doRetrieveMatchingFiles(String fullPattern, File dir, Set<Resource> result) throws IOException {

        if (logger.isTraceEnabled()) {
            logger.trace("Searching directory [" + dir.getAbsolutePath() + "] for files matching pattern [" + fullPattern + "]");
        }
        for (File content : listDirectory(dir)) {
            // "/" 左斜杠为Ant匹配规则 这里需要替换
            String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
            if (content.isDirectory() && pathMatcher.matchStart(fullPattern, currPath + "/")) {
                if (!content.canRead()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping subdirectory [" + dir.getAbsolutePath()
                                + "] because the application is not allowed to read the directory");
                    }
                } else {
                    doRetrieveMatchingFiles(fullPattern, content, result);
                }
            }
            if (pathMatcher.match(fullPattern, currPath)) {
                result.add(new FileSystemResource(content));
            }
        }
    }

    //过滤空目录
    protected File[] listDirectory(File dir) {

        File[] files = dir.listFiles();
        if (null == files) {
            if (logger.isInfoEnabled()) {
                logger.info("Could not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
            }
            return new File[0];
        }
        Arrays.sort(files, Comparator.comparing(File::getName));
        return files;
    }

    /**
     * 获取根路径
     * ？   匹配一个字符
     * *    匹配0个或多个字符
     * **   匹配0个或多个目录
     */
    private String determineRootDir(String location) {
        int rootDirEnd = location.indexOf('*');
        int zi = location.indexOf('?');
        if (zi != -1 && zi < rootDirEnd) {
            rootDirEnd = location.lastIndexOf('/', zi);
        }
        if (rootDirEnd != -1) {
            return location.substring(0, rootDirEnd);
        } else {
            return location;
        }
    }

}
