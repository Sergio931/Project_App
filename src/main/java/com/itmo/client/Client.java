package com.itmo.client;

import com.itmo.app.Handler;
import com.itmo.utils.SerializationManager;
import com.itmo.commands.Command;
import com.itmo.exceptions.StackIsLimitedException;
import com.itmo.server.Response;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.*;

/**
 * класс клиента
 */
public class Client {
    private SocketAddress socketAddress;
    private DatagramSocket socket;

    @Getter @Setter
    private Handler handler;

    @Getter
    private User user;
    private SerializationManager<Command> commandSerializationManager = new SerializationManager<>();
    private SerializationManager<Response> responseSerializationManager = new SerializationManager<>();
    private int scriptCount = 0;
    private static final int STACK_SIZE = 10;
    private static final int DEFAULT_BUFFER_SIZE = 65536;

    //подключаемся к заданному порту и хосту
    public void connect(String host, int port) throws IOException {
        try {
            InetAddress address = InetAddress.getByName(host);
            if (address == null) throw new NullPointerException();
            System.out.println("Подключение к " + address);
            socketAddress = new InetSocketAddress(address, port);
            socket = new DatagramSocket();
            socket.connect(socketAddress);
        } catch (NullPointerException e) {
            System.out.println("Введенного адреса не существует!!!");
        }
    }

    //отправляем команду и ждем ответа
    public Response sendCommandAndReceiveAnswer(Command command) throws IOException, ClassNotFoundException{
        //try {
            command.setUser(user);
            byte[] commandInBytes = commandSerializationManager.writeObject(command);
            DatagramPacket packet = new DatagramPacket(commandInBytes, commandInBytes.length, socketAddress);
            socket.send(packet);
            byte[] answerInBytes = new byte[DEFAULT_BUFFER_SIZE];
            packet = new DatagramPacket(answerInBytes, answerInBytes.length);
            socket.receive(packet);
            Response response = responseSerializationManager.readObject(answerInBytes);
            if (user == null) {
                user = response.getUser();
            }
            return response;
            /*
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Сервер в данный момент недоступен...");
        } catch (ClassNotFoundException e) {
            System.out.println("Клиент ждал ответ в виде Response, а получил что-то непонятное...");

        }
        */
    }

    public int getScriptCount() {
        return scriptCount;
    }

    public void incrementScriptCounter() {
        if (scriptCount >= STACK_SIZE) throw new StackIsLimitedException();
        scriptCount++;
    }

    public void decrementScriptCounter() {
        scriptCount--;
    }
}
