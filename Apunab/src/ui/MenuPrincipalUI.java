package ui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import model.Usuario;
import service.UsuarioService;

public class MenuPrincipalUI extends JFrame {
    private Usuario usuario;
    private UsuarioService usuarioService = new UsuarioService();
    private JPanel content;
    private boolean modoOscuro = false;

    private final Color claroFondo = new Color(234, 246, 255);
    private final Color claroTarjeta = Color.white;
    private final Color claroTexto = Color.black;

    private final Color oscuroFondo = new Color(25, 25, 25);
    private final Color oscuroTarjeta = new Color(45, 45, 45);
    private final Color oscuroTexto = Color.white;




    public MenuPrincipalUI(Usuario u) {
        this.usuario = u;
        setTitle("APUNAB - Dashboard");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // Sidebar (celeste)
        JPanel sidebar = new JPanel(new GridLayout(7, 1, 6, 6));
        sidebar.setPreferredSize(new Dimension(220, 720));
        sidebar.setBackground(new Color(84, 167, 255));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] items = {"Dashboard", "Ruleta", "Apuesta Popular", "Lugares", "Ranking", "Perfil", "FAQ"};
        for (String it : items) {
            JButton b = new JButton(it);
            b.setFocusPainted(false);
            b.setBackground(Color.white);
            b.setForeground(Color.black);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.addActionListener(e -> showPanel(it));
            sidebar.add(b);
        }
        JButton btnModo = new JButton("Modo Claro/Oscuro");
        btnModo.setFocusPainted(false);
        btnModo.setBackground(Color.white);
        btnModo.setForeground(Color.black);
        btnModo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnModo.addActionListener(e -> cambiarModo());
        sidebar.add(btnModo);
        add(sidebar, BorderLayout.WEST);

        // Content (CardLayout)
        content = new JPanel(new CardLayout());
        content.add(createDashboard(), "Dashboard");
        content.add(createRuleta(), "Ruleta");
        content.add(createApuestaPopular(), "Apuesta Popular");
        content.add(createLugares(), "Lugares");
        content.add(createRanking(), "Ranking");
        content.add(createPerfil(), "Perfil");
        content.add(createFAQ(), "FAQ");

        add(content, BorderLayout.CENTER);
    }

    private void showPanel(String key) {
        CardLayout cl = (CardLayout) content.getLayout();
        cl.show(content, key);
    }

    // DASHBOARD celeste con tarjetas y mini perfil+logo
    private JPanel createDashboard() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(234, 246, 255));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setBounds(30, 20, 400, 40);
        p.add(title);

        p.add(makeCard("APUNAB actuales", String.valueOf(usuario.getApunab()), 30, 100));
        p.add(makeCard("Promedio semanal", String.valueOf(rand(1000, 2000)), 360, 100));
        p.add(makeCard("Promedio mensual", String.valueOf(rand(5000, 7000)), 690, 100));
        p.add(makeCard("Promedio semestre", String.valueOf(rand(9000, 15000)), 30, 260));
        p.add(makeCard("Promedio anual", String.valueOf(rand(20000, 30000)), 360, 260));
        p.add(makeCard("Para aprobar DAR", "100000 APUNAB", 690, 260)); // DAR

        // Mini perfil con avatar + logo UNAB (logo_unab.png in data/)
        JPanel mini = new JPanel(null);
        mini.setBounds(920, 20, 340, 140);
        mini.setBackground(Color.white);
        mini.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 255)));

        ImageIcon avatarIcon = new ImageIcon("data/avatar.png");
        Image avatarImg = avatarIcon.getImage().getScaledInstance(72, 72, Image.SCALE_SMOOTH);
        JLabel avatar = new JLabel(new ImageIcon(avatarImg));
        avatar.setBounds(15, 30, 72, 72);
        mini.add(avatar);

        JLabel name = new JLabel(usuario.getNombre());
        name.setBounds(100, 28, 220, 24);
        mini.add(name);

        JLabel email = new JLabel(usuario.getEmail());
        email.setBounds(100, 56, 220, 20);
        mini.add(email);

        JLabel code = new JLabel("Código: " + usuario.getCodigo());
        code.setBounds(100, 84, 220, 20);
        mini.add(code);

        ImageIcon logo = new ImageIcon("data/logo_unab.png");
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBounds(260, 8, 72, 72); // top-right of mini
        mini.add(logoLabel);

        p.add(mini);
        return p;
    }

    private JPanel makeCard(String title, String value, int x, int y) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 300, 130);
        card.setBackground(Color.white);
        card.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 255)));
        JLabel t = new JLabel(title); t.setBounds(10, 10, 260, 22); t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel v = new JLabel(value); v.setBounds(10, 55, 260, 40); v.setFont(new Font("Segoe UI", Font.BOLD, 28));
        card.add(t); card.add(v);
        return card;
    }
    // RULETA: modo oscuro, número del día arriba, suma 30 APUNAB si cae en 18
// RULETA FUNCIONAL: gira, genera número 1-31 y da 30 si cae en 18
    private JPanel createRuleta() {
        JPanel p = new JPanel(null);

        // Fondo dinámico según modo
        p.setBackground(modoOscuro ? new Color(25,25,25) : new Color(234,246,255));

        JLabel title = new JLabel("Ruleta 1 - 31");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(modoOscuro ? Color.white : Color.black);
        title.setBounds(30, 20, 400, 40);
        p.add(title);

        // Número grande en el centro
        JLabel numero = new JLabel("?", SwingConstants.CENTER);
        numero.setFont(new Font("Segoe UI", Font.BOLD, 110));
        numero.setForeground(modoOscuro ? Color.white : Color.black);
        numero.setBounds(450, 120, 350, 180);
        p.add(numero);

        JButton btn = new JButton("Girar");
        btn.setBounds(550, 330, 150, 45);
        btn.setBackground(new Color(84,167,255));
        btn.setForeground(Color.white);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));

        btn.addActionListener(e -> {

            // Animación simple de giro
            new Thread(() -> {
                Random r = new Random();
                for (int i = 0; i < 18; i++) {
                    int randomSpin = r.nextInt(31) + 1;
                    numero.setText(String.valueOf(randomSpin));
                    try { Thread.sleep(70); } catch (Exception ex) {}
                }

                // Número final
                int resultado = r.nextInt(31) + 1;
                numero.setText(String.valueOf(resultado));

                // Si cae en 18 → da 30 APUNAB
                if (resultado == 18) {

                    usuario.setApunab(usuario.getApunab() + 30);

                    List<model.Usuario> list = usuarioService.allUsers();
                    for (model.Usuario uu : list) {
                        if (uu.getCodigo().equals(usuario.getCodigo())) {
                            uu.setApunab(usuario.getApunab());
                        }
                    }
                    usuarioService.updateAll(list);

                    JOptionPane.showMessageDialog(this,
                            "¡Cayó en 18! Ganaste 30 APUNAB 🎉",
                            "Ganaste",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Cayó en " + resultado + ". Solo da puntos si es 18.",
                            "Resultado",
                            JOptionPane.PLAIN_MESSAGE
                    );
                }

            }).start();
        });

        p.add(btn);

        return p;
    }


    private void cambiarModo() {
        modoOscuro = !modoOscuro;

        Color fondo = modoOscuro ? oscuroFondo : claroFondo;
        Color tarjeta = modoOscuro ? oscuroTarjeta : claroTarjeta;
        Color texto = modoOscuro ? oscuroTexto : claroTexto;

        // Fondo general
        content.setBackground(fondo);

        // Aplicar recursivamente a todos los paneles del CardLayout
        for (Component comp : content.getComponents()) {
            aplicarModo(comp, fondo, tarjeta, texto);
        }

        repaint();
        revalidate();
    }

    private void aplicarModo(Component c, Color fondo, Color tarjeta, Color texto) {

        if (c instanceof JPanel) {
            c.setBackground(fondo);

            for (Component child : ((JPanel) c).getComponents()) {
                aplicarModo(child, fondo, tarjeta, texto);
            }
        }

        if (c instanceof JLabel) {
            c.setForeground(texto);
        }

        if (c instanceof JButton) {
            c.setBackground(tarjeta);
            c.setForeground(texto);
        }
    }



    // Apuesta popular: solo texto limpio (no \n visibles, rendered normally)
    private JPanel createApuestaPopular() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(234, 246, 255));
        JLabel h = new JLabel("APUESTA POPULAR");
        h.setFont(new Font("Segoe UI", Font.BOLD, 22));
        h.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        p.add(h, BorderLayout.NORTH);

        JLabel text = new JLabel("<html><div style='width:700px;'>¿Podrás pasar Cálculo con 4.0 o más? Si lo logras, podrías duplicar tus APUNAB del semestre.</div></html>");
        text.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        text.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.add(text, BorderLayout.CENTER);
        return p;
    }

    // Lugares: botón "Fui hoy (+65)"
    private JPanel createLugares() {
        JPanel p = new JPanel(new GridLayout(5, 1, 8, 8));
        p.setBackground(new Color(234, 246, 255));
        String[] lugares = {"Cafetería del L", "Cafetería CSU", "Cafetería Bosque", "Cafetería Casona", "Banú"};
        for (String loc : lugares) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(Color.white);
            row.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 255)));
            JLabel label = new JLabel("  " + loc);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            row.add(label, BorderLayout.CENTER);

            JButton btn = new JButton("Fui hoy (+65)");
            btn.setBackground(new Color(84, 167, 255));
            btn.setForeground(Color.white);
            btn.addActionListener(e -> {
                usuario.setApunab(usuario.getApunab() + 65);
                List<model.Usuario> list = usuarioService.allUsers();
                for (model.Usuario uu : list) if (uu.getCodigo().equals(usuario.getCodigo())) uu.setApunab(usuario.getApunab());
                usuarioService.updateAll(list);
                JOptionPane.showMessageDialog(this, "Ganaste 65 APUNAB por visitar " + loc);
            });
            row.add(btn, BorderLayout.EAST);
            p.add(row);
        }
        return p;
    }

    // Ranking: vertical, 5 personas fixed + user last
    private JPanel createRanking() {
        JPanel p = new JPanel(new GridLayout(5, 1, 6, 6));
        p.setBackground(new Color(234, 246, 255));
        String[] lines = new String[] {
                "1. Massa - 12500 APUNAB",
                "2. Felipe - 9300 APUNAB",
                "3. Corredor - 800 APUNAB",
                "4. Diego Mora - 400 APUNAB",
                "5. " + usuario.getNombre() + " - " + usuario.getApunab() + " APUNAB"
        };
        for (String s : lines) {
            JLabel l = new JLabel(s);
            l.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            p.add(l);
        }
        return p;
    }

    private JPanel createPerfil() {
        JPanel p = new JPanel(new GridLayout(4, 1, 6, 6));
        p.setBackground(new Color(234, 246, 255));
        p.add(new JLabel("Nombre: " + usuario.getNombre()));
        p.add(new JLabel("Código: " + usuario.getCodigo()));
        p.add(new JLabel("Email: " + usuario.getEmail()));
        p.add(new JLabel("APUNAB: " + usuario.getApunab()));
        return p;
    }

    // FAQ: each question + answer shown
    private JPanel createFAQ() {
        JPanel p = new JPanel(new GridLayout(3, 1, 6, 6));
        p.setBackground(new Color(234, 246, 255));

        JPanel q1 = new JPanel(new BorderLayout());
        q1.setBackground(Color.white);
        q1.setBorder(BorderFactory.createLineBorder(new Color(180,210,255)));
        JLabel q1t = new JLabel("¿Qué son los APUNAB?");
        q1t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel q1a = new JLabel("<html>Son puntos estudiantiles obtenidos por actividades, apuestas y visitas a lugares UNAB.</html>");
        q1a.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        q1.add(q1t, BorderLayout.NORTH); q1.add(q1a, BorderLayout.CENTER);

        JPanel q2 = new JPanel(new BorderLayout());
        q2.setBackground(Color.white);
        q2.setBorder(BorderFactory.createLineBorder(new Color(180,210,255)));
        JLabel q2t = new JLabel("¿Cómo gano puntos?");
        q2t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel q2a = new JLabel("<html>Visitando lugares, usando la ruleta o participando en apuestas populares.</html>");
        q2a.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        q2.add(q2t, BorderLayout.NORTH); q2.add(q2a, BorderLayout.CENTER);

        JPanel q3 = new JPanel(new BorderLayout());
        q3.setBackground(Color.white);
        q3.setBorder(BorderFactory.createLineBorder(new Color(180,210,255)));
        JLabel q3t = new JLabel("¿Dónde puedo usarlos?");
        q3t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel q3a = new JLabel("<html>Para rankings, progresión y cumplir los 100.000 necesarios para el DAR.</html>");
        q3a.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        q3.add(q3t, BorderLayout.NORTH); q3.add(q3a, BorderLayout.CENTER);

        p.add(q1); p.add(q2); p.add(q3);
        return p;
    }

    private int rand(int a, int b) {
        return a + new Random().nextInt(b - a + 1);
    }
}
