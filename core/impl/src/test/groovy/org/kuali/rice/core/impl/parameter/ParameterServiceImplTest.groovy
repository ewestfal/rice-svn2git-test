package org.kuali.rice.core.impl.parameter

import groovy.mock.interceptor.MockFor
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.kuali.rice.kns.bo.PersistableBusinessObject
import org.kuali.rice.kns.service.BusinessObjectService
import org.kuali.rice.core.api.parameter.*

class ParameterServiceImplTest {
    private def MockFor mock

    //importing the should fail method since I don't want to extend
    //GroovyTestCase which is junit 3 style
    private final shouldFail = new GroovyTestCase().&shouldFail

    private static final Parameter parameter = createParameter();
    private static final ParameterKey key =
        ParameterKey.create("BORG_HUNT", "TNG", "C", "SHIELDS_ON");

    private ParameterServiceImpl pservice;
    private ParameterBo bo = ParameterBo.from(parameter)

    @Before
    void setupBoServiceMockContext() {
        mock = new MockFor(BusinessObjectService)
        pservice = new ParameterServiceImpl()
    }

    @Test
    void test_create_parameter_null_parameter() {
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);

        shouldFail(IllegalArgumentException.class) {
            pservice.createParameter(null)
        }
        mock.verify(boService)
    }

    @Test
    void test_create_parameter_exists() {
        mock.demand.findByPrimaryKey (1..1) { clazz, map -> bo }

        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);

        shouldFail(IllegalStateException.class) {
            pservice.createParameter(parameter)
        }
        mock.verify(boService)
    }

    @Test
    void test_create_parameter_does_not_exist() {
        mock.demand.findByPrimaryKey (2..2) { clazz, map -> null }
        mock.demand.save { PersistableBusinessObject bo -> }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        pservice.createParameter(parameter)
        mock.verify(boService)
    }

    @Test
    void test_update_parameter_null_parameter() {
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);

        shouldFail(IllegalArgumentException.class) {
            pservice.updateParameter(null)
        }
        mock.verify(boService)
    }

    @Test
    void test_update_parameter_exists() {
        mock.demand.findByPrimaryKey (1..1) { clazz, map -> bo }
        mock.demand.save { bo -> }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        pservice.updateParameter(parameter)
        mock.verify(boService)
    }

    @Test
    void test_update_parameter_does_not_exist() {
        mock.demand.findByPrimaryKey (2..2) { clazz, map -> null }

        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);

        shouldFail(IllegalStateException.class) {
            pservice.updateParameter(parameter)
        }
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_null_key() {
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);

        shouldFail(IllegalArgumentException.class) {
            pservice.getParameter(null)
        }
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_exists() {
        mock.demand.findByPrimaryKey (1..1) { clazz, map -> bo }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        Assert.assertEquals (parameter, pservice.getParameter(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_does_not_exist() {
        mock.demand.findByPrimaryKey (2..2) { clazz, map -> null }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        Assert.assertNull (pservice.getParameter(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_value_as_string_not_null() {
        mock.demand.findByPrimaryKey (1..1) { clazz, map -> bo }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        Assert.assertEquals (parameter.value, pservice.getParameterValueAsString(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_value_as_string_null() {
        mock.demand.findByPrimaryKey (2..2) { clazz, map -> null }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        Assert.assertNull (pservice.getParameterValueAsString(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_value_as_boolean_null() {
        mock.demand.findByPrimaryKey (2..2) { clazz, map -> null }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        Assert.assertNull (pservice.getParameterValueAsBoolean(key))
        mock.verify(boService)
    }

    private test_get_parameter_value_as_boolean_not_null(String value, boolean bValue) {
        ParameterBo bo = ParameterBo.from(parameter);
        bo.value = value

        mock.demand.findByPrimaryKey (1..1) { clazz, map -> bo }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        Assert.assertEquals(bValue, pservice.getParameterValueAsBoolean(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_value_as_boolean_not_null_Y() {
        test_get_parameter_value_as_boolean_not_null("Y", true)
    }

    @Test
    void test_get_parameter_value_as_boolean_not_null_true() {
        //mixed case
        test_get_parameter_value_as_boolean_not_null("tRue", true)
    }

    @Test
    void test_get_parameter_value_as_boolean_not_null_N() {
        test_get_parameter_value_as_boolean_not_null("N", false)
    }

    @Test
    void test_get_parameter_value_as_boolean_not_null_false() {
        //mixed case
        test_get_parameter_value_as_boolean_not_null("fAlse", false)
    }

    @Test
    void test_get_parameter_value_as_boolean_not_null_not_boolean() {
        bo.value = "not boolean"

        mock.demand.findByPrimaryKey (1..1) { clazz, map ->  bo}
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        Assert.assertNull (pservice.getParameterValueAsBoolean(key))
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_values_as_string_null() {
        mock.demand.findByPrimaryKey (2..2) { clazz, map ->  null}
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        def values = pservice.getParameterValuesAsString(key)
        Assert.assertTrue(values.isEmpty())

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_values_as_string_not_null_multiple_values() {
        //adding whitespace
        bo.value = "foo; bar ; baz "

        mock.demand.findByPrimaryKey (1..1) { clazz, map ->  bo}
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        def values = pservice.getParameterValuesAsString(key)
        Assert.assertTrue(values.size() == 3 )
        Assert.assertTrue(values.contains("foo"))
        Assert.assertTrue(values.contains("bar"))
        Assert.assertTrue(values.contains("baz"))

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    @Test
    void test_get_parameter_values_as_string_not_null_single_values() {
        //adding whitespace
        bo.value = "a value "

        mock.demand.findByPrimaryKey (1..1) { clazz, map ->  bo}
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        def values = pservice.getParameterValuesAsString(key)
        Assert.assertTrue(values.size() == 1)
        Assert.assertTrue(values.contains("a value"))

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_value_as_string_null_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValueAsString(key, null)
        }
    }

    @Test
    void test_get_sub_parameter_value_as_string_empty_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValueAsString(key, "")
        }
    }

    @Test
    void test_get_sub_parameter_value_as_string_whitespace_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValueAsString(key, "  ")
        }
    }

    @Test
    void test_get_sub_parameter_value_as_string_null() {
        mock.demand.findByPrimaryKey (2..2) { clazz, map ->  null}
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        Assert.assertNull(pservice.getSubParameterValueAsString(key, "foo"))
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_value_as_string_single_match() {
        //adding whitespace
        bo.value = "foo= f1; bar=b1; baz=z1"

        mock.demand.findByPrimaryKey (1..1) { clazz, map ->  bo}
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);

        Assert.assertEquals("f1", pservice.getSubParameterValueAsString(key,"foo"))
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_value_as_string_multiple_match() {
        bo.value = "foo=f1; bar=b1; foo=f2"

        mock.demand.findByPrimaryKey (1..1) { clazz, map ->  bo}
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);

        //should return first match
        Assert.assertEquals("f1", pservice.getSubParameterValueAsString(key,"foo"))
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_values_as_string_null_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValuesAsString(key, null)
        }
    }

    @Test
    void test_get_sub_parameter_values_as_string_empty_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValuesAsString(key, "")
        }
    }

    @Test
    void test_get_sub_parameter_values_as_string_whitespace_subparameter() {
        shouldFail(IllegalArgumentException) {
            pservice.getSubParameterValuesAsString(key, "  ")
        }
    }

    @Test
    void test_get_sub_parameter_values_as_string_null() {
        mock.demand.findByPrimaryKey (2..2) { clazz, map ->  null}
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);
        def values =  pservice.getSubParameterValuesAsString(key,"foo")
        Assert.assertTrue(values.isEmpty())

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_values_as_string_single_match() {
        //adding whitespace
        bo.value = "foo= f1, f2 , f3; bar=b1; baz=z1"

        mock.demand.findByPrimaryKey (1..1) { clazz, map ->  bo}
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);

        def values =  pservice.getSubParameterValuesAsString(key,"foo")
        Assert.assertTrue(values.size() == 3 )
        Assert.assertTrue(values.contains("f1"))
        Assert.assertTrue(values.contains("f2"))
        Assert.assertTrue(values.contains("f3"))

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    @Test
    void test_get_sub_parameter_values_as_string_multiple_match() {
        //adding whitespace
        bo.value = "foo= f1, f2 , f3; bar=b1; foo=f4,f5"

        mock.demand.findByPrimaryKey (1..1) { clazz, map ->  bo}
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService);

        def values =  pservice.getSubParameterValuesAsString(key,"foo")
        Assert.assertTrue(values.size() == 3 )
        Assert.assertTrue(values.contains("f1"))
        Assert.assertTrue(values.contains("f2"))
        Assert.assertTrue(values.contains("f3"))

        //is this unmodifiable?
        shouldFail(UnsupportedOperationException.class) {
            values.add("")
        }
        mock.verify(boService)
    }

    private static createParameter() {
		return Parameter.Builder.create(new ParameterContract() {
			def String name = "SHIELDS_ON"
			def ParameterType getParameterType() { ParameterType.Builder.create(new ParameterTypeContract() {
				def String code ="PC"
				def String name = "Config"
				def boolean active = true
			}).build()
            }
            def String applicationCode = "BORG_HUNT"
            def String namespaceCode = "TNG"
            def String componentCode = "C"
            def String value = "true"
            def String description = "turn the shields on"
            def EvaluationOperator evaluationOperator = EvaluationOperator.ALLOW
        }).build()
	}
}
