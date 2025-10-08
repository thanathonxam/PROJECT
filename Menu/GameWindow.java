package Menu;
import javax.swing.*;
import End.*;
import java.awt.*;
import UI.*;
import setBackgroud.*;
import java.awt.event.*;

public class GameWindow extends JFrame {
	
	public GameWindow() {
		setTitle("CHESS GAME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1920, 1080);
		setLocationRelativeTo(null);
		setResizable(false);
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
		int stopButtonY = 1080 - 24 - stopButtonH - 55; 
        
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
				// สร้างหน้าต่าง Pause Menu
					JDialog pauseDialog = new javax.swing.JDialog(GameWindow.this, "Game Paused", true);
					pauseDialog.setSize(400, 200);
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

					// ปุ่ม Resume
					resumeBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ev) {
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

					// ปุ่ม Exit
					exitBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ev) {
						System.exit(0); // ปิดเกมทั้งหมด
					}
					});
				pauseDialog.add(resumeBtn);
				pauseDialog.add(DrawBtn);
				pauseDialog.add(exitBtn);
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
		player1Active.setVisible(false);
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

		// ====== ใส่คอมโพเนนต์ลงแต่ละกรอบให้ตรง ======
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
		ChessBoard chessBoard = new ChessBoard(
				player1Active, timer1Label,
				player2Active, timer2Label,
				messageArea
		);
		add(chessBoard, BorderLayout.CENTER);

		setVisible(true);

        }
	
}