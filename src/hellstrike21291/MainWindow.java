package hellstrike21291;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

public class MainWindow {
	
	public static void main(String[] args) {
		try(DatagramSocket sock = new DatagramSocket(60000)) {
			
			
			LinkedList<InetAddress> clients = new LinkedList<InetAddress>();
			System.out.println("Создан пустой список клиентов");
			
			boolean isExist = false;
			System.out.println("Создан флаг проверки существования клиента");
			
			byte[] map = new byte[256];
			System.out.println("Создана байтовая карта");

			byte[] buf = new byte[256];
			System.out.println("Создан буфер для приема сообщения");

			DatagramPacket pack = new DatagramPacket(buf, 256);
			System.out.println("Создан UDP пакет для приема сообщения");
			
			System.out.println("===========================================");
			while(true) {
				
				isExist = false;
				
				pack.setData(buf);
				pack.setLength(256);
				System.out.println("Ожидаются данные");
				sock.receive(pack);
				System.out.println("Получено: " + pack.getLength() + " bytes");

				if(pack.getLength() != 2) {
					System.out.println("!!!Неверная длина полученного пакета!!!");
					continue;
				}
				
				for(InetAddress addr : clients) {
					
					if(addr.toString().compareTo(pack.getAddress().toString()) == 0) {
						System.out.println("Клиент уже соединен!");
						isExist = true;
					}
				}
				
				if(!isExist) {
					clients.add(pack.getAddress());
					System.out.println("Клиент добавлен в список соединений!");
				}
				
				map[pack.getData()[0]] = pack.getData()[1];
				System.out.println("Карта преобразована по запросу");
				
				pack.setData(map);
				pack.setLength(256);
				pack.setPort(7000);
				System.out.println("Карта подготовлена для отправки на порт " + pack.getPort());
				
				for(InetAddress addr : clients) {
					pack.setAddress(addr);
					sock.send(pack);
					System.out.println("Пакет отправлен на адрес: " + pack.getAddress());
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
