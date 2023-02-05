package lab.oxgame;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.net.SyslogOutputStream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lab.oxgame.dao.RozgrywkaDAO;
import lab.oxgame.dao.RozgrywkaDAOImpl;
import lab.oxgame.engine.OXGame;
import lab.oxgame.engine.OXGameImpl;
import lab.oxgame.model.OXEnum;
import lab.oxgame.model.Rozgrywka;

public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	@FXML
	Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8;
	
	@FXML
	private Label lblinfo;
	
	@FXML
	private Button btnReset, btnClear;
	
	@FXML TextField txtGraczO, txtGraczX;
	
	@FXML
	private TableView<Rozgrywka> rozgrywkaTable;
	
	@FXML
	private TableColumn<Rozgrywka, Integer> rozgrywkaIdColumn;
	
	@FXML
	private TableColumn<Rozgrywka, String> graczXColumn;
	
	@FXML
	private TableColumn<Rozgrywka, String> graczOColumn;
	
	@FXML
	private TableColumn<Rozgrywka, OXEnum> zwyciezcaColumn;
	
	@FXML
	private TableColumn<Rozgrywka, LocalDateTime> dataczasRozgrywkiColumn;
	
	private ObservableList<Rozgrywka> history;
	
	private ExecutorService wykonawca;
	
	RozgrywkaDAO rozgrywkaDAO;
	OXGame oxGame;
	
	public MainController() {
		rozgrywkaDAO = new RozgrywkaDAOImpl();
		oxGame = new OXGameImpl();
	}
	
	public MainController(RozgrywkaDAO rozgrywkaDAO) {
		this.rozgrywkaDAO = rozgrywkaDAO;
	}
	
	public MainController(RozgrywkaDAO rozgrywkaDAO, OXGame gameImpl) {
		this.rozgrywkaDAO = rozgrywkaDAO;
		this.oxGame = gameImpl;
	}
	
	@FXML
	public void initialize()
	{
		rozgrywkaIdColumn.setCellValueFactory(new PropertyValueFactory<Rozgrywka, Integer>("rozgrywkaId"));
		graczOColumn.setCellValueFactory(new PropertyValueFactory<Rozgrywka, String>("graczO"));
		graczXColumn.setCellValueFactory(new PropertyValueFactory<Rozgrywka, String>("graczX"));
		zwyciezcaColumn.setCellValueFactory(new PropertyValueFactory<Rozgrywka, OXEnum>("zwyciezca"));
		dataczasRozgrywkiColumn.setCellValueFactory(new PropertyValueFactory<Rozgrywka, LocalDateTime>("dataczasRozgrywki"));
		
		history = FXCollections.observableArrayList();
		rozgrywkaTable.setItems(history);

		wykonawca = Executors.newSingleThreadExecutor();
		wykonawca.execute(() -> {
			List<Rozgrywka> rozgrywki = rozgrywkaDAO.pobierzRozgrywki(0, 100);
			if (rozgrywki != null)
			{
				Platform.runLater(() -> {
					history.addAll(rozgrywki);
				});
			}
		});
	}
	
	@FXML
	public void onActionBtnClear(ActionEvent event)
	{
		wykonawca = Executors.newSingleThreadExecutor();
		wykonawca.execute(() -> {
			int rozgrywki = rozgrywkaDAO.usunRozgrywki();
			if (rozgrywki > 0)
			{
				history.clear();
			}
		});
	}
	
	@FXML
	public void onActionBtnReset(ActionEvent event){
		if(txtGraczO.getText().isEmpty() && txtGraczX.getText().isEmpty())
		{
			lblinfo.setText("Podaj imiona graczy!");
		}
		else
		{
			oxGame.inicjalizuj();
			lblinfo.setText("Kolej gracza "+oxGame.getKolej().toString());
			btn0.setText("");
			btn1.setText("");
			btn2.setText("");
			btn3.setText("");
			btn4.setText("");
			btn5.setText("");
			btn6.setText("");
			btn7.setText("");
			btn8.setText("");
		}
	}
	
	void ruch(Button btn)
	{
		btn.setText(oxGame.getKolej().toString());
		switch(btn.getId()) {
			case "btn0":
				oxGame.setPole(0);
				break;
			case "btn1":
				oxGame.setPole(1);	
				break;
			case "btn2":
				oxGame.setPole(2);
				break;
			case "btn3":
				oxGame.setPole(3);
				break;
			case "btn4":
				oxGame.setPole(4);
				break;
			case "btn5":
				oxGame.setPole(5);
				break;
			case "btn6":
				oxGame.setPole(6);
				break;
			case "btn7":
				oxGame.setPole(7);
				break;
			case "btn8":
				oxGame.setPole(8);
				break;
		}
		lblinfo.setText("Kolej gracza "+oxGame.getKolej().toString());
	}
	
	public void onActionBtn(ActionEvent event)
	{
		if(oxGame.getZwyciezca() == null)
		{
			Button btn = (Button)event.getSource();
			if(btn.getText().isEmpty())
			{
				ruch(btn);
			}
			if(oxGame.getZwyciezca() != null)
			{
				lblinfo.setText("Wygra³ gracz "+oxGame.getZwyciezca().toString());
				wykonawca.execute(() -> {
					Rozgrywka rozgrywka = new Rozgrywka(txtGraczX.getText(), txtGraczO.getText(), oxGame.getZwyciezca(), LocalDateTime.now());
					rozgrywkaDAO.zapiszRozgrywke(rozgrywka);
					if(rozgrywka.getRozgrywkaId() != null)
					{
						Platform.runLater(() -> {
							history.add(0,rozgrywka);
						});
					}
					else
					{
						logger.error("Wynik rozgrywki nie zosta³ zapisany w bazie danych!");
					}
				});
			}
		}			
	}

	public void testBazy() {
		Rozgrywka rozgrywka = new Rozgrywka("graczX", "graczO", OXEnum.X, LocalDateTime.now());
		int altered = rozgrywkaDAO.zapiszRozgrywke(rozgrywka);
		logger.info("Liczba dodanych wierszy: {}, ID: {}", altered,rozgrywka.getRozgrywkaId());
		
		List<Rozgrywka> rozgrywki = rozgrywkaDAO.pobierzRozgrywki(0, 100);
		for(Rozgrywka r: rozgrywki)
		{
			logger.info("-> rozgrywka_id: {}, data: {}", r.getRozgrywkaId(),r.getDataczasRozgrywki());
		}
		
		int removed = rozgrywkaDAO.usunRozgrywki();
		logger.info("Usuwanie wszystkich rozgrywek! (usuniêto: {})!",removed);
	}
	
	public void shutdown() {
		wykonawca.shutdown();
	}
}
