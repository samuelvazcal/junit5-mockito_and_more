package org.svazquez.junit5app.ejemplos.models;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.svazquez.junit5app.ejemplos.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {
    Cuenta cuenta;
    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeEach
    @Test
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter) {
        this.cuenta = new Cuenta("Sofia", new BigDecimal("1000.12345"));
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        System.out.println("Iniciando el metodo.");
        System.out.println(" ejecutando: " + testInfo.getDisplayName() + " " + Objects.requireNonNull(testInfo.getTestMethod().orElse(null)).getName() +
                " con las etiquetas " + testInfo.getTags());
        // reportando a un log usando testReporter
        testReporter.publishEntry(" reportando usando TestReporter: " + testInfo.getDisplayName() + " " + Objects.requireNonNull(testInfo.getTestMethod().orElse(null)).getName() +
                " con las etiquetas " + testInfo.getTags());
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el metodo de prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("***Inicializando el test***");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("---Finalizando el test---");
    }

    @Tag("cuenta")
    @Nested
    @DisplayName("Probando atributos de cuenta corriente")
    class CuentaTestNombreSaldo {
        @Test
        @DisplayName("el nombre!")
        void testNombreCuenta() {
            testReporter.publishEntry(testInfo.getTags().toString());
            if(testInfo.getTags().contains("cuenta")) {
                System.out.println("Haciendo mayusculas a la etiqueta! " + getClass().getSimpleName().toUpperCase());
            }
//        cuenta.setPersona("Sofia");
            String esperado = "Sofia";
            String real = cuenta.getPersona();
            assertNotNull(real, "la cuenta no puede ser nula");
            assertEquals(esperado, real, "el nombre de la cuenta no es el que se esperaba: se esperaba " + esperado);
            assertTrue(real.equals("Sofia"),"nombre cuenta esperada debe ser igual a la real");
        }

        @Test
        @DisplayName("el saldo!")
        void testSaldoCuenta() {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        }

        @Test
        void testReferenciaCuenta() {
            Cuenta cuenta1 = new Cuenta("Sofia Vazquez", new BigDecimal("8900.9997"));
            Cuenta cuenta2 = new Cuenta("Sofia Vazquez", new BigDecimal("8900.9997"));
            //assertNotEquals(cuenta2,cuenta1);
            assertEquals(cuenta2, cuenta1);
        }
    }

    @Nested
    class CuentaOperacionesTest {
        @Test
        void testDebitoCuenta() {
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

        @Test
        void testCreditoCuenta() {
            cuenta.credito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
        }

        @Tag("cuenta")
        @Tag("banco")
        @Test
        void testTransferirDineroCuentas() {
            Cuenta cuenta1 = new Cuenta("Sofia", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Angel", new BigDecimal("1500.8989"));
            Banco banco = new Banco();
            banco.setNombre("Citi Banamex");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
            assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
            assertEquals("3000", cuenta1.getSaldo().toPlainString());
        }
    }

    @Tag("cuenta")
    @Tag("error")
    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero insuficiente";
        assertEquals(esperado, actual);
    }


    @Tag("cuenta")
    @Tag("banco")
    @Test
    //@Disabled
    void testRelacionBancoCuentas() {
        //fail();
        Cuenta cuenta1 = new Cuenta("Sofia", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Angel", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Citi Banamex");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertAll(() -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString()),
                () -> assertEquals("3000", cuenta1.getSaldo().toPlainString()),
                () -> assertEquals(2, banco.getCuentas().size()),
                () -> assertEquals("Citi Banamex", cuenta1.getBanco().getNombre()),
                () -> assertEquals("Sofia", banco.getCuentas().stream()
                        .filter(f -> f.getPersona().equals("Sofia"))
                        .findFirst()
                        .get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream()
                        .anyMatch(a -> a.getPersona().equals("Angel"))));
    }

    @Nested
    class SistemaOperativoTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }

        @Test
        @EnabledOnOs({OS.LINUX,OS.MAC})
        void testSoloLinuxMac() {
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void testSoloJdk8() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_11)
        void testSoloJdk11() {
        }
    }

    @Nested
    class SystemPropertiesTest {
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k,v) -> System.out.println(k + ": " + v));
        }

        @Test
        @EnabledIfSystemProperty(named="user.country",matches = "US")
        void testUserCountry() {
        }

        @Test
        @EnabledIfSystemProperty(named="user.language",matches = "es")
        void testUserLanguage() {
        }

        @Test
        @DisabledIfSystemProperty(named="os.arch",matches = ".*64")
        void testSolo64() {
        }
    }

    @Nested
    class VariableAmbienteTest {
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k,v) -> System.out.println(k + ": " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME" , matches = ".*jdk11.0.8.*")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "5")
        void testProcesadores() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT",matches = "dev")
        void testEnv() {
        }
    }

    @Test
    @DisplayName("test Saldo Cuenta Dev")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(esDev);
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("test Saldo Cuenta Dev 2")
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(esDev, () -> {
            assertNotNull(cuenta.getSaldo());
        });
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @DisplayName("Probando Debito Cuenta Repetir!")
    @RepeatedTest(value=7, name = "Repetición numero {currentRepetition} de {totalRepetitions}")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if(info.getCurrentRepetition()==3) System.out.println("Esta es la repetición " + info.getCurrentRepetition());
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Tag("param")
    @Nested
    class PruebasParametrizadasTest{
        @ParameterizedTest
        @ValueSource(strings = {"100","200","300","500","700","1000","1000.12345"})
        void testDebitoCuentaValueSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "número {index} ejecutando con valor {0}")
        @CsvSource({"1,100","2,200","3,300","4,500","5,700","6,1000","7,1000.12345"})
        void testDebitoCuentaCsvSource(String index, String monto) {
            System.out.println(index + " -> " + monto);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "número {index} ejecutando con valor {0}")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoCuentaCsvFileSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "número {index} ejecutando con valor {0}")
        @CsvSource({"200,100","250,200","300,300","510,500","750,700","1000.12345,1000.12345"})
        void testDebitoCuentaCsvSource2(String saldo, String monto) {
            System.out.println(saldo + " -> " + monto);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "número {index} ejecutando con valor {0}")
        @CsvSource({"200,100,Samuel,Manuel","250,200,Paulina,paulina","500,300,Lucas,Lucas"})
        void testDebitoCuentaCsvSource3(String saldo, String monto, String esperado, String actual) {
            System.out.println(saldo + " -> " + monto);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);

            assertNotNull(cuenta.getSaldo());
            assertNotNull(cuenta.getPersona());
            assertEquals(esperado,actual);

            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "número {index} ejecutando con valor {0}")
        @CsvFileSource(resources = "/data2.csv")
        void testDebitoCuentaCsvSource4(String saldo, String monto, String esperado, String actual) {
            System.out.println(saldo + " -> " + monto);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);

            assertNotNull(cuenta.getSaldo());
            assertNotNull(cuenta.getPersona());
            assertEquals(esperado,actual);

            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }
    }


    @Tag("param")
    @ParameterizedTest(name = "número {index} ejecutando con valor {0}")
    @MethodSource("montoList")
    void testDebitoCuentaMethodSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    static List<String> montoList() {
        return Arrays.asList("100","200","300","500","700","1000","1000.12345");
    }

    @Nested
    @Tag("timeout")
    class EjemploTimeOut {
        @Test
        @Timeout(2)
        void testTimeOut() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

        @Test
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
        void testTimeOut2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(990);
        }

        @Test
        void testTimeOutAssertions() {
            assertTimeout(Duration.ofSeconds(6),() -> {
                TimeUnit.MILLISECONDS.sleep(5700);
            });
        }
    }

}