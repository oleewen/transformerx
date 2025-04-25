package com.transformer.context;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.core.env.ConfigurableEnvironment;

public class EnvironmentTest {
    private Environment instance;

    @Before
    public void setUp() throws Exception {
        instance = new Environment();
    }

    @After
    public void tearDown() throws Exception {
        instance.configChange();
        instance = null;
    }

    @org.junit.Test
    public void configChange() {
    }

    @org.junit.Test
    public void testGetProperty() {
        ApplicationEnvironmentPreparedEvent event = Mockito.mock(ApplicationEnvironmentPreparedEvent.class);
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(event.getEnvironment()).thenReturn(environment);
        Mockito.when(environment.getProperty("key")).thenReturn("value");
        instance.onApplicationEvent(event);

        String value = Environment.getProperty("key");

        Assert.assertEquals("value", value);

        value = Environment.getProperty("key");

        Assert.assertEquals("value", value);

        Mockito.verify(environment, Mockito.times(1)).getProperty("key");
    }

    @org.junit.Test
    public void testGetPropertyDefault() {
        ApplicationEnvironmentPreparedEvent event = Mockito.mock(ApplicationEnvironmentPreparedEvent.class);
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(event.getEnvironment()).thenReturn(environment);
        Mockito.when(environment.getProperty("key-default", "value")).thenReturn("value");
        instance.onApplicationEvent(event);

        String value = Environment.getProperty("key-default", "value");

        Assert.assertEquals("value", value);

        value = Environment.getProperty("key-default");

        Assert.assertEquals("value", value);

        Mockito.verify(environment, Mockito.times(1)).getProperty("key-default", "value");
    }

    @org.junit.Test
    public void testGetIntProperty() {
        ApplicationEnvironmentPreparedEvent event = Mockito.mock(ApplicationEnvironmentPreparedEvent.class);
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(event.getEnvironment()).thenReturn(environment);
        Mockito.when(environment.getProperty("key1")).thenReturn("1");
        instance.onApplicationEvent(event);

        Integer value = Environment.getIntProperty("key1");

        Assert.assertEquals(Integer.valueOf(1), value);

        value = Environment.getIntProperty("key1");

        Assert.assertEquals(Integer.valueOf(1), value);

        Mockito.verify(environment, Mockito.times(1)).getProperty("key1");
    }

    @org.junit.Test
    public void testGetBooleanProperty() {
        ApplicationEnvironmentPreparedEvent event = Mockito.mock(ApplicationEnvironmentPreparedEvent.class);
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(event.getEnvironment()).thenReturn(environment);
        Mockito.when(environment.getProperty("key2")).thenReturn("true");
        instance.onApplicationEvent(event);

        boolean value = Environment.getBooleanProperty("key2");

        Assert.assertTrue(value);

        value = Environment.getBooleanProperty("key2");

        Assert.assertTrue(value);

        Mockito.verify(environment, Mockito.times(1)).getProperty("key2");
    }

    @org.junit.Test
    public void getApplicationId() {
    }

    @org.junit.Test
    public void getRegistryAddress() {
    }

    @org.junit.Test
    public void isDev() {
        ApplicationEnvironmentPreparedEvent event = Mockito.mock(ApplicationEnvironmentPreparedEvent.class);
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(event.getEnvironment()).thenReturn(environment);
        Mockito.when(environment.getActiveProfiles()).thenReturn(new String[]{Env.DEV.getEnvName()});
        instance.onApplicationEvent(event);

        boolean value = Environment.isDev();

        Assert.assertTrue(value);

        value = Environment.isDev();

        Assert.assertTrue(value);

        Mockito.verify(environment, Mockito.times(1)).getActiveProfiles();
    }

    @org.junit.Test
    public void isFat() {
        ApplicationEnvironmentPreparedEvent event = Mockito.mock(ApplicationEnvironmentPreparedEvent.class);
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(event.getEnvironment()).thenReturn(environment);
        Mockito.when(environment.getActiveProfiles()).thenReturn(new String[]{Env.FAT.getEnvName()});

        instance.onApplicationEvent(event);

        boolean value = Environment.isFat();

        Assert.assertTrue(value);

        value = Environment.isFat();

        Assert.assertTrue(value);

        Mockito.verify(environment, Mockito.times(1)).getActiveProfiles();
    }

    @org.junit.Test
    public void isPre() {
        ApplicationEnvironmentPreparedEvent event = Mockito.mock(ApplicationEnvironmentPreparedEvent.class);
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(event.getEnvironment()).thenReturn(environment);
        Mockito.when(environment.getActiveProfiles()).thenReturn(new String[]{Env.PRE.getEnvName()});

        instance.onApplicationEvent(event);

        boolean value = Environment.isPre();

        Assert.assertTrue(value);

        value = Environment.isPre();

        Assert.assertTrue(value);

        Mockito.verify(environment, Mockito.times(1)).getActiveProfiles();
    }

    @org.junit.Test
    public void isProd() {
        ApplicationEnvironmentPreparedEvent event = Mockito.mock(ApplicationEnvironmentPreparedEvent.class);
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(event.getEnvironment()).thenReturn(environment);
        Mockito.when(environment.getActiveProfiles()).thenReturn(new String[]{Env.PRODUCT.getEnvName()});

        instance.onApplicationEvent(event);

        boolean value = Environment.isProd();

        Assert.assertTrue(value);

        value = Environment.isProd();

        Assert.assertTrue(value);

        Mockito.verify(environment, Mockito.times(1)).getActiveProfiles();
    }
}