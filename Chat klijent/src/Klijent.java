import java.io.*;
import java.net.*;

public class Klijent extends Thread {

	static Socket soket = null;
	static PrintStream izlazniTokKaServeru = null;
	static BufferedReader ulaznTokOdServera = null;
	static BufferedReader ulazSaKonzole = null;
	static boolean kraj = false;
	static DatagramSocket dSoket = null;
	static byte[] podaciOdServera = null;
	static byte[] podaciZaServer = null;
	static DatagramPacket paketOdServera = null;
	static DatagramPacket paketZaServer = null;

	public static void main(String[] args) {
		int port = 6666;
		try {
			soket = new Socket("localhost", port);
			ulazSaKonzole = new BufferedReader(new InputStreamReader(System.in));
			izlazniTokKaServeru = new PrintStream(soket.getOutputStream());
			ulaznTokOdServera = new BufferedReader(new InputStreamReader(soket.getInputStream()));

			new Thread(new Klijent()).start();

			dSoket = new DatagramSocket();
			getInformationUDP();
			InetAddress IPAddress = InetAddress.getByName("localhost");
			podaciZaServer = new byte[1024];
			String starter = "obavestavam te o mom broju porta.";
			podaciZaServer = starter.getBytes();
			paketZaServer = new DatagramPacket(podaciZaServer, podaciZaServer.length, IPAddress, 7777);
			dSoket.send(paketZaServer);
			System.out.println("poslao starter");

			while (!kraj) {
				izlazniTokKaServeru.println(ulazSaKonzole.readLine());
			}

			soket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void getInformationUDP() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (!kraj) {
					try {

						podaciOdServera = new byte[1024];
						paketOdServera = new DatagramPacket(podaciOdServera, podaciOdServera.length);
						dSoket.receive(paketOdServera);
						String listaKlijenata = new String(paketOdServera.getData());
						System.out.println("Klijenti: " + listaKlijenata);

					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {

					}
				}
			}
		});
		t.start();
	}

	@Override
	public void run() {
		String linijaOdServera;

		try {
			while ((linijaOdServera = ulaznTokOdServera.readLine()) != null) {
/*
				if (linijaOdServera.contains("///linija")) {
					izlazniTokKaServeru.println("///potvrda");
					linijaOdServera = linijaOdServera.replaceAll("///linija", " ");
				}
*/
				System.out.println(linijaOdServera);

				if (linijaOdServera.contains("///Dovidjenja")) {
					kraj = true;
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
