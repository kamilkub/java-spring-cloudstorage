package com.cloudstorage.controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class JSONConverter<T> {


    public T writeStringToObject(String jsonData, Class clazz) {

        try {
            AtomicReference<T> instance = new AtomicReference<>((T) clazz.getConstructor(null).newInstance(null));

            List<ParamValue> paramValueList = new BasicJsonReader().readJson(jsonData);

            paramValueList.forEach(paramValue -> {
                String paramName = paramValue.getParameterName();
                paramName = paramName.replace("\"", "");
                paramName = paramName.replace(paramName.charAt(0), (char) ((int) paramName.charAt(0) - 32));
                try {
                    Method setMethod = clazz.getMethod("set" + paramName, int.class);

                    instance.set((T) setMethod.invoke(instance.get(), Integer.valueOf(paramValue.getParameterValue())));

                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });


            return instance.get();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected static class BasicJsonReader {
        public List<ParamValue> readJson(String json) {
            List<ParamValue> paramValueList = new ArrayList<>();
            json = json.replaceAll("\\s", "");
            json = json.replaceAll("\\{|\\}", "");

            String [] keyValuePairs = json.split(",");

            Arrays.stream(keyValuePairs).forEach(pair -> {
                ParamValue paramValue = new ParamValue();

                String splitPair [] = pair.split(":");

                paramValue.setParameterName(splitPair[0]);
                paramValue.setParameterValue(splitPair[1]);

                paramValueList.add(paramValue);
            });

            return paramValueList;
        }
    }

    static class ParamValue {
        private String parameterName;
        private String parameterValue;

        public String getParameterName() {
            return parameterName;
        }

        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }

        public String getParameterValue() {
            return parameterValue;
        }

        public void setParameterValue(String parameterValue) {
            this.parameterValue = parameterValue;
        }
    }
}