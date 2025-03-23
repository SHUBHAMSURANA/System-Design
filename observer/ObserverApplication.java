@SpringBootApplication
public class ObserverApplication {

	public static void main(String[] args) {
		NewsAgency observable = new NewsAgency();
		
		NewsChannel observer1 = new NewsChannel();
		SportsChannel observer2 = new SportsChannel();

		// Adding the subscriber 
		observable.addObserver(observer1);
		observable.addObserver(observer2);

		// Now both above class will be notofied and news will be set in one shot. that is the magic
		observable.setNews("news");
		System.out.println(observer.getNews());
	}

}
