package com.sun.network.rpc;

import com.sun.network.rpc.user.Cat;
import com.sun.network.rpc.user.CatImpl;
import com.sun.network.rpc.user.StrCater;
import com.sun.network.rpc.user.StrCaterImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        //testSocket();
        //testRPC();
        testRPCStrCater();

    }

    private static void testSocket() throws InterruptedException {

        RPCServer rpcServer = new RPCServer(9090, null);
        rpcServer.start();
        System.out.println("rpcServer started");

        RPCClient rpcClient = new RPCClient("localhost", 9090);

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes("zinc".getBytes());
        rpcClient.write(buffer);

    }

    private static void testRPC() throws InterruptedException {

        RPCProvider rpcProvider = new RPCProvider();
        rpcProvider.register(Cat.class.getName(), new CatImpl());

        RPCServer rpcServer = new RPCServer(9090, rpcProvider);
        rpcServer.start();
        System.out.println("rpcServer started");

        RPCConsumer consumer = new RPCConsumer("localhost", 9090);
        Cat cat = consumer.get(Cat.class);
        String miao = cat.miao();

        System.out.println("RPC returned: " + miao);
    }

    @Test
    private static void testRPCStrCater() throws InterruptedException {

        RPCProvider rpcProvider = new RPCProvider();
        rpcProvider.register(StrCater.class.getName(), new StrCaterImpl());

        RPCServer rpcServer = new RPCServer(9090, rpcProvider);
        rpcServer.start();
        System.out.println("rpcServer started");

        RPCConsumer consumer = new RPCConsumer("localhost", 9090);
        StrCater cater = consumer.get(StrCater.class);

        List<String> list = new ArrayList<>(10);
        list.add("sun");
        list.add("zinc");
        list.add("zjdif");
        list.add("zifjfijdfidjfisdjifjiu8r9vn8envnduvi");
        list.add("82u3r8ujijijijidfsfs122233333333333333333333333333333333333333456666666666666");
        list.add("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        list.add("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        list.add("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        list.add("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        list.add("ppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");

        String result = cater.cat(list);

        System.out.println("RPC returned: " + result);
    }
}

