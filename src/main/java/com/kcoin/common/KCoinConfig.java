package com.kcoin.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;
import com.kcoin.common.interceptor.CorsInterceptor;
import com.kcoin.common.model._MappingKit;

/**
 * KCoin Config
 */
public class KCoinConfig extends JFinalConfig {


    /**
     * Start or Debug by right click
     *
     * @param args
     */
    public static void main(String[] args) {
        /**
         * For Eclipse
         */
        //JFinal.start("src/main/webapp", 80, "/", 5);

        /**
         * For IDEA
         */
        JFinal.start("src/main/webapp", 8089, "/");
    }

    /**
     * Constant val
     * <p>
     * Get value by PropKit.get(...)
     */
    public void configConstant(Constants constants) {
        PropKit.use("configs.properties");
        constants.setDevMode(PropKit.getBoolean("devMode", false));
        constants.setJsonFactory(MixedJsonFactory.me());
    }

    /**
     * Route config
     */
    public void configRoute(Routes routes) {
        routes.add(new FrontRoutes());
    }

    public void configEngine(Engine engine) {
    }

    /**
     * Plugin config
     */
    public void configPlugin(Plugins plugins) {
        // druid
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
        druidPlugin.setDriverClass("org.sqlite.JDBC");
        plugins.add(druidPlugin);

        // ActiveRecord
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        // set table fields to upperCase
        arp.setContainerFactory(new CaseInsensitiveContainerFactory(false));
        // set sql template path
        arp.getEngine().setSourceFactory(new ClassPathSourceFactory());
        arp.addSqlTemplate("/sql/All.sql");
        //映射表到model
        _MappingKit.mapping(arp);
        //事物隔离级别
        arp.setTransactionLevel(4);
        // MappingKit
        _MappingKit.mapping(arp);
        plugins.add(arp);
    }

    public static DruidPlugin getDruidPlugin() {
        PropKit.use("configs.properties");
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
        druidPlugin.setDriverClass("org.sqlite.JDBC");
        return druidPlugin;
    }

    /**
     * Global Interceptor
     */
    public void configInterceptor(Interceptors interceptors) {
        interceptors.addGlobalActionInterceptor(new CorsInterceptor());
    }

    /**
     * Global Handler
     */
    public void configHandler(Handlers handlers) {

    }
}
