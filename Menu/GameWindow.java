package Menu;
import javax.swing.*;
import End.*;
import Start.Start;
import java.awt.*;
import UI.*;
import setBackgroud.*;
import java.awt.event.*;

public class GameWindow extends JFrame {
	private ChessBoard chessBoard;
	private java.io.File saveFile = new java.io.File("Save/board.csv");
	private void ensureSaveDir() {
    if (saveFile.getParentFile() != null && !saveFile.getParentFile().exists()) {
        saveFile.getParentFile().mkdirs();
    	}
	}

	private void saveGameToCSV() {
    	try {
        	ensureSaveDir();
        	try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.OutputStreamWriter(
                new java.io.FileOutputStream(saveFile), java.nio.charset.StandardCharsets.UTF_8))) {

            // 8 แถวของกระดาน
            for (int r = 0; r < 8; r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < 8; c++) {
                    sb.append(chessBoard.encodeAt(r, c));
                    if (c<7) sb.append(',');
                }
                pw.println(sb.toString());
            }

            // TURN
            ChessPiece.Color turn = chessBoard.getCurrentTurn();
            String turnStr;
			if (turn == ChessPiece.Color.WHITE) {
    			turnStr = "WHITE";
			} else {
    			turnStr = "BLACK";
			}
			pw.println("TURN," + turnStr);

            // TIME
            int w = 600, b = 600; // default กันพัง
            if (chessBoard.getGameClock() != null) {
                int[] t = chessBoard.getGameClock().getTimes();
                w = t[0]; b = t[1];
            }
            pw.println("TIME," + w + "," + b);
        	}
    	} catch (Exception ex) {
        	ex.printStackTrace();
        	JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage());
    	}
	}

	private boolean loadGameFromCSV() {
    if (saveFile == null || !saveFile.exists()) return false;
    try (java.io.BufferedReader br = new java.io.BufferedReader(
			new java.io.InputStreamReader(new java.io.FileInputStream(saveFile), java.nio.charset.StandardCharsets.UTF_8))) {

        String[][] cells = new String[8][8];
        for (int r=0; r<8; r++) {
            String line = br.readLine();
            if (line == null) return false;
            String[] toks = line.split(",", -1);
            if (toks.length != 8) return false;
            cells[r] = toks;
        }

        String turnLine = br.readLine();     // "TURN,WHITE"
        String timeLine = br.readLine();     // "TIME,600,600"
        if (turnLine == null || timeLine == null) return false;

        // เติมหมากลงกระดาน
        for (int r=0; r<8; r++) {
            for (int c=0; c<8; c++) {
                chessBoard.decodeToCell(r, c, cells[r][c]);
            }
        }

        // TURN
        String[] tt = turnLine.split(",", -1);
        if (tt.length>=2) {
            ChessPiece.Color turn;
    		if ("WHITE".equalsIgnoreCase(tt[1])) {
        		turn = ChessPiece.Color.WHITE;
    		} else {
        		turn = ChessPiece.Color.BLACK;
    		}
    		chessBoard.setCurrentTurn(turn);
        }
		// ให้ GameClock รู้ตาเดินปัจจุบันด้วย
		if (chessBoard.getGameClock() != null) {
    		chessBoard.getGameClock().setCurrentTurn(chessBoard.getCurrentTurn());
		}

		// อัปเดตป้าย ACTIVE ให้ตรงกับ currentTurn
		chessBoard.updatePlayerLabelStyles();

        // TIME
        String[] tm = timeLine.split(",", -1);
        if (tm.length>=3 && chessBoard.getGameClock()!=null) {
            int w = Integer.parseInt(tm[1].trim());
            int b = Integer.parseInt(tm[2].trim());
            chessBoard.getGameClock().setTimes(new int[]{w,b});
        }

        chessBoard.forceRefresh();  // << เรียกตัวใหม่ที่เราเพิ่งเพิ่ม
        return true;

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Load failed: " + ex.getMessage());
        return false;
    }
}

	public GameWindow() {
		setTitle("CHESS GAME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1920, 1080);
		setLocationRelativeTo(null);
		setResizable(false);
		setUndecorated(true);
		setLayout(new BorderLayout());

		//สร้าง icon เเละแสดงผล
		ImageIcon icon = new ImageIcon("Image/icon.png");
		setIconImage(icon.getImage());

		// --- Panel ข้างๆ (พื้นหลังโทนเข้ม) ---
		JPanel sidePanel = new BackgroundPanel("Image/sidepanel.png");
		sidePanel.setOpaque(false); 			// สำคัญ: ต้องโปร่งใส
		sidePanel.setPreferredSize(new Dimension(300, 1080));
		sidePanel.setLayout(null); 					// สำคัญ: ใช้ null layout ที่ panel นี้

		add(sidePanel, BorderLayout.WEST);

		int x = 24;
		int w = 252;
		int topY = 24;
		int topH = 210;
		int gap = 10;
        
		int stopButtonW = w; 
		int stopButtonH = 60; 
		int stopButtonX = x; 
		int stopButtonY = 1080 - 24 - stopButtonH - 20; 
        
		JButton StopButton = new JButton("STOP GAME");
		StopButton.setFont(new Font("Arial", Font.BOLD, 30));	
		StopButton.setBackground(new Color(255, 69, 0)); // สีแดง/ส้ม
		StopButton.setForeground(Color.WHITE);
		StopButton.setPreferredSize(new Dimension(250, 60));	
		StopButton.setBorder(BorderFactory.createRaisedBevelBorder());	
		StopButton.setFocusable(false);
		StopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chessBoard.setBoardEnabled(false);
				StopButton.setEnabled(false);
				chessBoard.pauseClock();           

				JDialog pauseDialog = new javax.swing.JDialog(GameWindow.this, "Game Paused");
				pauseDialog.setSize(300, 500);
				pauseDialog.setLayout(new java.awt.GridLayout(3, 1, 10, 10));
				pauseDialog.setLocationRelativeTo(GameWindow.this);

				JButton resumeBtn = new javax.swing.JButton("Resume Game");
				resumeBtn.setFont(new Font("Arial", Font.BOLD, 30));	
				resumeBtn.setBackground(new Color(46, 204, 113));
				resumeBtn.setPreferredSize(new Dimension(250, 60));	
				resumeBtn.setBorder(BorderFactory.createRaisedBevelBorder());	
				resumeBtn.setForeground(Color.WHITE);

				JButton DrawBtn = new javax.swing.JButton("Draw Game");
				DrawBtn.setFont(new Font("Arial", Font.BOLD, 30));	
				DrawBtn.setBackground(new Color(30, 144, 255));
				DrawBtn.setPreferredSize(new Dimension(250, 60));	
				DrawBtn.setBorder(BorderFactory.createRaisedBevelBorder());	
				DrawBtn.setForeground(Color.WHITE);

				JButton exitBtn = new javax.swing.JButton("Exit Game");
				exitBtn.setFont(new Font("Arial", Font.BOLD, 30));	
				exitBtn.setBackground(new Color(255, 69, 0));
				exitBtn.setPreferredSize(new Dimension(250, 60));	
				exitBtn.setBorder(BorderFactory.createRaisedBevelBorder());	
				exitBtn.setForeground(Color.WHITE);	

				resumeBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ev) {
						StopButton.setEnabled(true);
						chessBoard.setBoardEnabled(true);
						chessBoard.resumeClock();
						pauseDialog.dispose();
						GameWindow.this.requestFocusInWindow();
					}
				});

				DrawBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ev) {
						new DrawGameWindow();
						pauseDialog.dispose();
						setVisible(false);
					}
				});
				
				exitBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ev) {
       					saveGameToCSV();
        				pauseDialog.dispose();
        				GameWindow.this.dispose();
				        new Start(); // กลับไปหน้า Start
					}
				});
				pauseDialog.add(resumeBtn);
				pauseDialog.add(DrawBtn);
				pauseDialog.add(exitBtn);
				pauseDialog.setUndecorated(true);
				pauseDialog.setVisible(true);	
			}
		});
		StopButton.setBounds(stopButtonX, stopButtonY, stopButtonW, stopButtonH);
		sidePanel.add(StopButton); 


		// --- Player Labels ---
		JLabel player1Name	 = new JLabel("PLAYER 1", SwingConstants.LEFT);
		JLabel player1Color	= new JLabel("(WHITE)",	 SwingConstants.LEFT);
		JLabel player1Active = new JLabel("ACTIVE",	 SwingConstants.LEFT);
		JLabel timer1Label	 = new JLabel("10:00",	 SwingConstants.LEFT);

		JLabel player2Name	 = new JLabel("PLAYER 2", SwingConstants.LEFT);
		JLabel player2Color	= new JLabel("(BLACK)",	 SwingConstants.LEFT);
		JLabel player2Active = new JLabel("ACTIVE",	 SwingConstants.LEFT);
		JLabel timer2Label	 = new JLabel("10:00",	 SwingConstants.LEFT);
		timer1Label.setOpaque(false);  
		timer2Label.setOpaque(false);  


		// ฟอนต์/สีให้ตัดกับพื้นหลังเข้ม
		Color text = new Color(220,230,240);
		Color sub	= new Color(170,180,190);
		player1Name.setFont(new Font("", Font.BOLD, 28));
		player2Name.setFont(new Font("", Font.BOLD, 28));
		player1Color.setFont(new Font("", Font.PLAIN, 24));
		player2Color.setFont(new Font("", Font.PLAIN, 24));
		player1Active.setFont(new Font("", Font.BOLD, 22));
		player2Active.setFont(new Font("", Font.BOLD, 22));
		timer1Label.setFont(new Font("", Font.PLAIN, 24));
		timer2Label.setFont(new Font("", Font.PLAIN, 24));
		player1Name.setForeground(text);
		player2Name.setForeground(text);
		player1Color.setForeground(sub);
		player2Color.setForeground(sub);
		timer1Label.setForeground(text);
		timer2Label.setForeground(text);
		player1Active.setForeground(new Color(0, 220, 80)); // เขียว
		player2Active.setForeground(new Color(0, 220, 80));
		player1Active.setVisible(true);
		player2Active.setVisible(false);

		// --- Game Log ---
		JTextPane messageArea = new JTextPane();
		messageArea.setEditable(false);
		messageArea.setFont(messageArea.getFont().deriveFont(14f));
		messageArea.setOpaque(false);	// ให้กลืนพื้นหลังได้เหมือนเดิม
		messageArea.setForeground(text);	// สีตัวอักษรปกติของ LOG (ใช้กับข้อความช่อง เช่น "e4")
		JScrollPane scrollPane = new JScrollPane(messageArea);
		messageArea.setFont(new Font("", Font.PLAIN, 20));
		messageArea.setForeground(text);
		messageArea.setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(BorderFactory.createTitledBorder("GAME LOG"));


		// **โค้ดที่แก้ไข: การคำนวณตำแหน่ง Panel ย่อย (ใช้ stopButtonY ที่ประกาศแล้ว)**
		int botH = 210;
		// ปรับ botY ให้ bottomPanel อยู่เหนือปุ่ม STOP (เว้น gap)
		int botY = stopButtonY - gap - botH; 			
		int logY = topY + topH + gap; 			
		// ปรับ logH ให้สั้นลง
		int logH = botY - gap - logY;	
		// **จบโค้ดที่แก้ไข**

		// ====== แผงย่อยโปร่งใส 3 ตัว ======
		JPanel topPanel = new JPanel(null); 	// null เพื่อใช้ setBounds ของ label ภายใน
		topPanel.setOpaque(false);
		topPanel.setBounds(x, topY, w, topH);
		sidePanel.add(topPanel);

		JPanel logPanel = new JPanel(null);
		logPanel.setOpaque(false);
		logPanel.setBounds(x, logY, w, logH);
		sidePanel.add(logPanel);

		JPanel bottomPanel = new JPanel(null);
		bottomPanel.setOpaque(false);
		bottomPanel.setBounds(x, botY, w, botH);
		sidePanel.add(bottomPanel);

		// TOP: PLAYER 2 (กรอบบน)
		player2Name.setBounds(10,	8, w-20, 36);
		player2Color.setBounds(10, 44, w-20, 26);
		player2Active.setBounds(10, 74, w-20, 26);
		timer2Label.setBounds(10, 104, w-20, 30);
		topPanel.add(player2Name);
		topPanel.add(player2Color);
		topPanel.add(player2Active);
		topPanel.add(timer2Label);

		// LOG: กลาง
		scrollPane.setBounds(0, 0, w, logH);
		logPanel.add(scrollPane);

		// BOTTOM: PLAYER 1 (กรอบล่าง)
		player1Name.setBounds(10,	8,	w-20, 36);
		player1Color.setBounds(10, 44, w-20, 26);
		player1Active.setBounds(10, 74, w-20, 26);
		timer1Label.setBounds(10, 104, w-20, 30);
		bottomPanel.add(player1Name);
		bottomPanel.add(player1Color);
		bottomPanel.add(player1Active);
		bottomPanel.add(timer1Label);

		// --- กระดานหมากรุกตรงกลาง ---
		chessBoard = new ChessBoard(
				player1Active, timer1Label,
				player2Active, timer2Label,
				messageArea
		);
		add(chessBoard, BorderLayout.CENTER);
		chessBoard.setFocusable(true);
		chessBoard.requestFocusInWindow();
		chessBoard.revalidate(); 
		chessBoard.repaint();
		chessBoard.updatePlayerLabelStyles();
		setVisible(true);
        }

	public GameWindow(boolean resume) {
    	this(); // สร้าง UI จากคอนสตรักเตอร์เดิม

    // โหลดไฟล์ถ้ากด Continue; แจ้งเตือนถ้าโหลดไม่ได้
    	if (resume && !loadGameFromCSV()) {
        	JOptionPane.showMessageDialog(this, "NO SAVED GAME FOUND!");
    	}

    // ดันงาน UI หลังหน้าต่างพร้อมจริง
    javax.swing.SwingUtilities.invokeLater(() -> {
        if (chessBoard != null) chessBoard.requestFocusInWindow();
        GameWindow.this.toFront();
        GameWindow.this.requestFocusInWindow();
    });
}
}



