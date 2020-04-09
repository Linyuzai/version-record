package com.github.linyuzai.versionrecord.core;

import com.github.linyuzai.versionrecord.annotation.EnableVersionRecord;
import com.github.linyuzai.versionrecord.annotation.VersionPoint;
import com.github.linyuzai.versionrecord.annotation.VersionPoints;
import com.github.linyuzai.versionrecord.filter.VersionRecordFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class VersionPointRegister implements ApplicationRunner, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(VersionPointRegister.class);

    private ApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Version record enabled");
        final List<VersionPointInformation> versions = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            Class<?> beanClass;
            try {
                beanClass = Class.forName(metadataReader.getAnnotationMetadata().getClassName());
            } catch (ClassNotFoundException e) {
                throw new VersionRecordException(e);
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
            VersionRecorder versionRecorder = context.getBean(VersionRecorder.class);
            Set<String> allSet = new HashSet<>();
            for (Map.Entry<String, Object> entry : entries) {
                EnableVersionRecord enableVersionRecord = context.findAnnotationOnBean(entry.getKey(), EnableVersionRecord.class);
                if (enableVersionRecord == null) {
                    throw new VersionRecordException("Could not happen");
                }
                versionRecorder.setFormatter(DateTimeFormatter.ofPattern(enableVersionRecord.dateFormatter()));
                versionRecorder.setFilter(context.getBean(VersionRecordFilter.class));
                String[] basePackages = enableVersionRecord.basePackages();
                if (basePackages.length == 0) {
                    allSet.add(entry.getValue().getClass().getPackage().getName());
                } else {
                    allSet.addAll(Arrays.asList(basePackages));
                }
            }
            Map<String, VersionPointScanPath> pathMap = context.getBeansOfType(VersionPointScanPath.class);
            for (VersionPointScanPath scanPath : pathMap.values()) {
                allSet.addAll(scanPath.getBasePackages());
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
            logger.info("Version record scan packages : {}", basePackages);
            logger.info("Version record start scanning...");
            for (String basePackage : basePackages) {
                provider.findCandidateComponents(basePackage);
            }
            versionRecorder.record(versions);
            logger.info("Version record scan finished");
        } else {
            throw new VersionRecordException("Multi @EnableVersionRecord found");
        }
    }

    private static List<VersionPointInformation> recordVersionOnClass(Class<?> cls) {
        return processVersionPoints(cls.getAnnotation(VersionPoint.class), cls.getAnnotation(VersionPoints.class), cls, null, null);
    }

    private static List<VersionPointInformation> recordVersionOnMethod(Class<?> cls, Method method) {
        return processVersionPoints(method.getAnnotation(VersionPoint.class), method.getAnnotation(VersionPoints.class), cls, method, null);
    }

    @Deprecated
    private static List<VersionPointInformation> recordVersionOnField(Class<?> cls, Field field) {
        return processVersionPoints(field.getAnnotation(VersionPoint.class), field.getAnnotation(VersionPoints.class), cls, null, field);
    }

    private static List<VersionPointInformation> processVersionPoints(VersionPoint vp, VersionPoints vps, Class<?> cls,
                                                                      Method method, Field field) {
        List<VersionPointInformation> vil = new ArrayList<>();
        if (vp != null) {
            vil.add(transferVersionInformation(vp, cls, method, field));
        }
        if (vps != null) {
            vil.addAll(Arrays.stream(vps.value())
                    .map(it -> transferVersionInformation(it, cls, method, field))
                    .collect(Collectors.toList()));
        }
        return vil;
    }

    private static VersionPointInformation transferVersionInformation(VersionPoint vp, Class<?> cls, Method method, Field field) {
        VersionPointInformation vi = new VersionPointInformation();
        vi.setVersion(vp.version());
        vi.setDescription(vp.description());
        vi.setBranch(vp.branch());
        vi.setAuthor(vp.author());
        vi.setDependServices(vp.dependServices());
        vi.setDependTables(vp.dependTables());
        vi.setDate(vp.date());
        vi.setLocations(new String[]{method == null ? cls.getName() : cls.getName() + "#" + method.getName()});
        return vi;
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        if (applicationContext == null) {
            throw new VersionRecordException("Application context is null");
        }
        this.context = applicationContext;
    }
}
