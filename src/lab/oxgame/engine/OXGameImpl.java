package lab.oxgame.engine;

import lab.oxgame.model.OXEnum;

public class OXGameImpl implements OXGame {

	//0 1 2
	//3 4 5
	//6 7 8
	private OXEnum[] plansza = new OXEnum[9];
	OXEnum kolej;
	OXEnum zwyciezca;
	int krok;
	
	@Override
	public void inicjalizuj() {
		krok = 0;
		zwyciezca = null;
		for(int i=0;i<9;i++)
		{
			plansza[i] = OXEnum.BRAK;
		}
		
		if(Math.random()<0.5)
		{
			kolej = OXEnum.X;
		}
		else
		{
			kolej = OXEnum.O;
		}
	}

	@Override
	public void setPole(int indeks) {
		plansza[indeks] = kolej;
		krok++;
		aktualizuj();
	}
	
	@Override
	public OXEnum getPole(int indeks) {
		return plansza[indeks];
	}

	@Override
	public OXEnum getKolej() {
		return kolej;
	}

	@Override
	public OXEnum getZwyciezca() {
		return zwyciezca;
	}

	private void aktualizuj()
	{
		//aktualizuj krok
		if(kolej.equals(plansza[0]) && kolej.equals(plansza[1]) && kolej.equals(plansza[2])
		|| (kolej == plansza[3] && kolej == plansza[4] && kolej == plansza[5])
		|| (kolej == plansza[6] && kolej == plansza[7] && kolej == plansza[8])
		|| (kolej == plansza[0] && kolej == plansza[3] && kolej == plansza[6])
		|| (kolej == plansza[1] && kolej == plansza[4] && kolej == plansza[7])
		|| (kolej == plansza[2] && kolej == plansza[5] && kolej == plansza[8])
		|| (kolej == plansza[0] && kolej == plansza[4] && kolej == plansza[8])
		|| (kolej == plansza[6] && kolej == plansza[4] && kolej == plansza[2]))
		{
			zwyciezca = kolej;
		}
		else if(krok>=9)
		{
			zwyciezca = OXEnum.BRAK;
			System.out.println("Remis");
			
		}
		if(kolej==OXEnum.X)
		{
			kolej = OXEnum.O;
		}
		else if(kolej==OXEnum.O)
		{
			kolej = OXEnum.X;
		}
	}
	
}
