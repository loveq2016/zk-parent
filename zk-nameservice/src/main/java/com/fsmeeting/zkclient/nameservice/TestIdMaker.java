package com.fsmeeting.zkclient.nameservice;


public class TestIdMaker {

    public static void main(String[] args) throws Exception {

        IdMaker idMaker = new IdMaker("192.168.1.105:2181",
                "/NameService/IdGen", "ID");
        idMaker.start();

        try {
            for (int i = 0; i < 10; i++) {
                String id = idMaker.generateId(IdMaker.RemoveMethod.DELAY);
                System.out.println(id);

            }
        } finally {
            idMaker.stop();

        }
    }

}
