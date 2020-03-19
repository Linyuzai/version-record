package com.github.linyuzai.versionrecord.core;

import com.github.linyuzai.versionrecord.annotation.EnableVersionRecord;
import com.github.linyuzai.versionrecord.annotation.VersionPoint;
import com.github.linyuzai.versionrecord.annotation.VersionPoints;
import com.github.linyuzai.versionrecord.filter.DefaultVersionRecordFilter;
import com.github.linyuzai.versionrecord.filter.VersionRecordFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class VersionPointRegister implements ApplicationRunner, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(VersionPointRegister.class);

    private ApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Version record start scanning...");
        final List<VersionPointInformation> versions = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            Class<?> beanClass;
            try {
                beanClass = Class.forName(metadataReader.getAnnotationMetadata().getClassName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            versions.addAll(recordVersionOnClass(beanClass));
            for (Method method : beanClass.getDeclaredMethods()) {
                versions.addAll(recordVersionOnMethod(beanClass, method));
            }
            return false;
        });
        Map<String, Object> beans = context.getBeansWithAnnotation(EnableVersionRecord.class);
        Set<Map.Entry<String, Object>> entries = beans.entrySet();
        if (entries.size() == 1) {
            Set<String> allSet = new HashSet<>();
            for (Map.Entry<String, Object> entry : entries) {
                EnableVersionRecord enableVersionRecord = context.findAnnotationOnBean(entry.getKey(), EnableVersionRecord.class);
                if (enableVersionRecord == null) {
                    throw new RuntimeException("Could not happen");
                }
                VersionRecorder.getInstance().setFormatter(DateTimeFormatter.ofPattern(enableVersionRecord.dateFormatter()));
                Class<? extends VersionRecordFilter> filterClass = enableVersionRecord.filter();
                if (filterClass == DefaultVersionRecordFilter.class) {
                    VersionRecorder.getInstance().setFilter(DefaultVersionRecordFilter.getInstance());
                } else {
                    VersionRecorder.getInstance().setFilter(filterClass.newInstance());
                }
                allSet.addAll(Arrays.asList(enableVersionRecord.basePackages()));
//                for (String basePackage : enableVersionRecord.basePackages()) {
//                    provider.findCandidateComponents(basePackage);
//                }
            }
            Map<String, VersionPointScanPath> pathMap = context.getBeansOfType(VersionPointScanPath.class);
            for (VersionPointScanPath scanPath : pathMap.values()) {
                allSet.addAll(scanPath.getBasePackages());
//                for (String basePackage : scanPath.getBasePackages()) {
//                    provider.findCandidateComponents(basePackage);
//                }
            }
            List<String> basePackages = new ArrayList<>();
            for (String one : allSet) {
                Iterator<String> iterator = basePackages.iterator();
                boolean needAdd = true;
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    if (one.startsWith(next)) {
                        needAdd = false;
                        break;
                    } else if (next.startsWith(one)) {
                        iterator.remove();
                        break;
                    }
                }
                if (needAdd) {
                    basePackages.add(one);
                }
            }
            for (String basePackage : basePackages) {
                provider.findCandidateComponents(basePackage);
            }
            VersionRecorder.getInstance().record(versions);
            logger.info("Version record scan finished");
        } else {
            throw new RuntimeException("Multi @EnableVersionRecord found");
        }
    }

    private static List<VersionPointInformation> recordVersionOnClass(Class<?> cls) {
        return processVersionPoints(cls.getAnnotation(VersionPoint.class), cls.getAnnotation(VersionPoints.class), cls, null);
    }

    private static List<VersionPointInformation> recordVersionOnMethod(Class<?> cls, Method method) {
        return processVersionPoints(method.getAnnotation(VersionPoint.class), method.getAnnotation(VersionPoints.class), cls, method);
    }

    private static List<VersionPointInformation> processVersionPoints(VersionPoint vp, VersionPoints vps, Class<?> cls, Method method) {
        List<VersionPointInformation> vil = new ArrayList<>();
        if (vp != null) {
            vil.add(transferVersionInformation(vp, cls, method));
        }
        if (vps != null) {
            vil.addAll(Arrays.stream(vps.value())
                    .map(it -> transferVersionInformation(it, cls, method))
                    .collect(Collectors.toList()));
        }
        return vil;
    }

    private static VersionPointInformation transferVersionInformation(VersionPoint vp, Class<?> cls, Method method) {
        VersionPointInformation vi = new VersionPointInformation();
        vi.setVersion(vp.version());
        vi.setDescription(vp.description());
        vi.setBranch(vp.branch());
        vi.setDependServices(vp.dependServices());
        vi.setDependTables(vp.dependTables());
        vi.setDate(vp.date());
        vi.setLocations(new String[]{method == null ? cls.getName() : cls.getName() + "#" + method.getName()});
        return vi;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
