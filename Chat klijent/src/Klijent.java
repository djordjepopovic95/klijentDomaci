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
	static DatagramPacket paketOdServera = null;

	public static void main(String[] args) {
		int port = 6666;
		try {
			soket = new Socket("localhost", port);
			ulazSaKonzole = new BufferedReader(new InputStreamReader(System.in));
			izlazniTokKaServeru = new PrintStream(soket.getOutputStream());
			ulaznTokOdServera = new BufferedReader(new InputStreamReader(soket.getInputStream()));

			new Thread(new Klijent()).start();
			getInformationUDP();

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

						dSoket = new DatagramSocket(6666);
						// InetAddress IPAddress =
						// InetAddress.getByName("localhost");
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
						dSoket.close();
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
				System.out.println(linijaOdServera);

				// izlazniTokKaServeru.println("1"); moram da razdvojim ulaz i
				// izlaz nekako
				if (linijaOdServera.contains("***Dovidjen")) {
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
