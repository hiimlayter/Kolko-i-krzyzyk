package lab.oxgame.engine;

import lab.oxgame.model.OXEnum;

public interface OXGame {

	//0,1,2, 3,4,5, 6,7,8
	void inicjalizuj(); //1. losowanie kolejnosci tj. X lub O
	
	void setPole(int indeks); // aktualizacja stanu gry
		//(zmiana kolejnosci, sprawdzanie czy jest zwyciezca)
	
	OXEnum getPole(int indeks); //w zasadzie niepotrzebma
	
	OXEnum getKolej(); //gdy OXEnum.Brak to koniec gry
	
	OXEnum getZwyciezca(); //na koniec sprawdzamy zwyciezce
	
}
