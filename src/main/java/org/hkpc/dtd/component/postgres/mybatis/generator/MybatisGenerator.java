package org.hkpc.dtd.component.postgres.mybatis.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MybatisGenerator {
    /**
     * You may be able to run this class just use JDK command with the right classpath.
     * But if you are running this class in IDEA in the project,to avoid the check of other unrelated classes, better: in the Edit of the "Run/Debug Configuration", click "Modify Options", deselect the "Do not build before run" option.
     */
    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<>(2);
        ConfigurationParser cp = new ConfigurationParser(warnings);

        File configFile = new File("src/main/resources/generator-configuration.xml");
        Configuration config = cp.parseConfiguration(configFile);

        DefaultShellCallback callback = new DefaultShellCallback(true);

        MyBatisGenerator myBatisGenerator= new MyBatisGenerator(config, callback, warnings);

        myBatisGenerator.generate(null);
    }
}
