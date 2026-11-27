/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package BK_SMKN1Dlanggu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author 16_Devin_X-RPL-3#
 */
public class laporan extends javax.swing.JFrame {
    private int selectedId = -1;
    private DefaultTableModel model;
    private int selectedIdLaporan;

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(laporan.class.getName());

    public laporan() {
        initComponents();
        

        ButtonGroup grupAngka = new ButtonGroup();
        grupAngka.add(jRadioButton1);
        grupAngka.add(jRadioButton2);
        grupAngka.add(jRadioButton3);

        ButtonGroup grupTingkat = new ButtonGroup();
        grupTingkat.add(jRadioButton4);
        grupTingkat.add(jRadioButton5);
        grupTingkat.add(jRadioButton6);

        // Mengatur tombol angka (rombel 3) berdasarkan jurusan
        cmbKelas.addActionListener(e -> {
            Object selected = cmbKelas.getSelectedItem();
            if (selected == null) return;
            String jurusan = selected.toString();

            if (jurusan.equals("DKV")
                    || jurusan.equals("PHT")
                    || jurusan.equals("ANM")) {
                jRadioButton3.setEnabled(false);
                if (jRadioButton3.isSelected()) {
                    grupAngka.clearSelection();
                }
            } else {
                jRadioButton3.setEnabled(true);
            }
        });

        model = new DefaultTableModel(
            new String[]{
                "NISN Siswa",
                "Nama",
                "Kelas/Jurusan",
                "Status",
                "Jenis Laporan",
                "Keterangan",
                "ID Laporan"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblLaporan.setModel(model);

        // Sembunyikan kolom ID Laporan (indeks 6)
        tblLaporan.getColumnModel().getColumn(6).setMinWidth(0);
        tblLaporan.getColumnModel().getColumn(6).setMaxWidth(0);
        tblLaporan.getColumnModel().getColumn(6).setWidth(0);

        muatDataTabel("");
    }
    

    // Dipanggil oleh frame Prestasi dan Pelanggaran untuk menyimpan ke database.
    public static boolean tambahDataLaporan(
            String nisnSiswa,
            String status,
            String jenisLaporan,
            String keterangan) {

        String sql = "INSERT INTO laporan (nisn, status, jenis_laporan, keterangan) VALUES (?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Koneksi database gagal!");
                return false;
            }

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, nisnSiswa.trim());
                pst.setString(2, status);
                pst.setString(3, jenisLaporan);
                pst.setString(4, keterangan);
                pst.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambah laporan: " + e.getMessage());
            return false;
        }
    }

 private void muatDataTabel(String kataKunci) {
        model.setRowCount(0);
        selectedId = -1;

        String sql = "SELECT l.id_laporan, l.nisn, s.nama, " +
                "CONCAT(s.jurusan, ' ', s.tingkat, '-', s.rombel) AS kelas, " +
                "l.status, l.jenis_laporan, l.keterangan " +
                "FROM laporan l " +
                "JOIN siswa s ON l.nisn = s.nisn " +
                "WHERE l.nisn LIKE ? OR s.nama LIKE ? " +
                "ORDER BY l.id_laporan DESC";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Koneksi database gagal!");
                return;
            }

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                String cari = "%" + kataKunci.trim() + "%";
                pst.setString(1, cari);
                pst.setString(2, cari);

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getString("nisn"),
                            rs.getString("nama"),
                            rs.getString("kelas"),
                            rs.getString("status"),
                            rs.getString("jenis_laporan"),
                            rs.getString("keterangan"),
                            rs.getInt("id_laporan")
                        });
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat laporan: " + e.getMessage());
        }
    }

    private void setPilihanKelas(String kelasLengkap) {
        // Contoh format: "RPL X-1"
        if (kelasLengkap == null || kelasLengkap.isBlank()) return;

        String[] bagian = kelasLengkap.trim().split("\\s+");
        if (bagian.length >= 1) {
            cmbKelas.setSelectedItem(bagian[0]);
        }
        if (bagian.length >= 2) {
            String[] tingkatRombel = bagian[1].split("-");
            if (tingkatRombel.length >= 1) {
                String tingkat = tingkatRombel[0];
                jRadioButton4.setSelected("X".equals(tingkat));
                jRadioButton5.setSelected("XI".equals(tingkat));
                jRadioButton6.setSelected("XII".equals(tingkat));
            }
            if (tingkatRombel.length >= 2) {
                int rombel = Integer.parseInt(tingkatRombel[1]);
                jRadioButton1.setSelected(rombel == 1);
                jRadioButton2.setSelected(rombel == 2);
                jRadioButton3.setSelected(rombel == 3);
            }
        }
    }

 private void kosongkanInput() {
        txtId.setText("");
        txtNama.setText("");
        cmbKelas.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        kt.setText("");
        tblLaporan.clearSelection();
        selectedId = -1;
    }

  public void refreshLaporan() {
        String kataKunci = (txtId != null) ? txtId.getText() : "";
        muatDataTabel(kataKunci);
    }                                                                                         
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbKelas = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        btnHapus = new javax.swing.JButton();
        btnCari = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLaporan = new javax.swing.JTable();
        btnEdit = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        kt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel1.setText("LAPORAN SISWA");

        jLabel2.setText("Kelas/Jurusan");

        cmbKelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RPL", "TKJ", "DKV", "PHT", "KL", "ANM" }));
        cmbKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKelasActionPerformed(evt);
            }
        });

        jLabel3.setText("Status ");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SANGAT BAIK", "BAIK", "BURUK", "SANGAT BURUK" }));
        cmbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStatusActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        jLabel5.setText("NISN");

        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        tblLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "NISN", "Nama", "Kelas/Jurusan", "Status", "Prestasi/Planggaran", "Keterangan"
            }
        ));
        tblLaporan.setMaximumSize(new java.awt.Dimension(8000, 97888));
        tblLaporan.setPreferredSize(new java.awt.Dimension(700, 80));
        tblLaporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLaporanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblLaporan);
        if (tblLaporan.getColumnModel().getColumnCount() > 0) {
            tblLaporan.getColumnModel().getColumn(0).setMaxWidth(70);
            tblLaporan.getColumnModel().getColumn(2).setPreferredWidth(110);
            tblLaporan.getColumnModel().getColumn(2).setMaxWidth(110);
            tblLaporan.getColumnModel().getColumn(3).setPreferredWidth(125);
            tblLaporan.getColumnModel().getColumn(3).setMaxWidth(125);
            tblLaporan.getColumnModel().getColumn(4).setPreferredWidth(210);
            tblLaporan.getColumnModel().getColumn(4).setMaxWidth(10000);
            tblLaporan.getColumnModel().getColumn(5).setMinWidth(30);
            tblLaporan.getColumnModel().getColumn(5).setMaxWidth(100);
        }

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        jLabel6.setText("Nama");

        txtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 51, 102));
        jButton1.setText("Exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jRadioButton1.setText("1");

        jRadioButton2.setText("2");

        jRadioButton4.setText("X");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jRadioButton3.setText("3");

        jRadioButton6.setText("XII");

        jRadioButton5.setText("XI");

        jLabel4.setText("Keterangan");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnHapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCari)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(25, 25, 25))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(jLabel1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6))
                                    .addGap(57, 57, 57))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNama, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(txtId)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jRadioButton1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jRadioButton2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jRadioButton4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jRadioButton5)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton6)
                                    .addComponent(jRadioButton3)))
                            .addComponent(kt))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel6)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton5))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(kt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHapus)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKelasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbKelasActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed

        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Apakah yakin ingin menghapus data ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM laporan WHERE id_laporan = ?";
            try (Connection conn = Koneksi.getConnection();
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, selectedId);
                pst.executeUpdate();

                muatDataTabel("");
                kosongkanInput();
                JOptionPane.showMessageDialog(this, "Data laporan berhasil dihapus.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
            }
        }
   // TODO add your handling code here:
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
    String nisn = txtId.getText().trim();

if (nisn.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Masukkan NISN terlebih dahulu!");
    return;
}

try {
    Connection conn = Koneksi.getConnection();

    String sql = "SELECT * FROM siswa WHERE nisn = ?";
    PreparedStatement pst = conn.prepareStatement(sql);
    pst.setString(1, nisn);

    ResultSet rs = pst.executeQuery();

    if (rs.next()) {
        txtNama.setText(rs.getString("nama"));
        cmbKelas.setSelectedItem(rs.getString("jurusan"));

        String tingkat = rs.getString("tingkat");
        int rombel = rs.getInt("rombel");

        jRadioButton4.setSelected("X".equals(tingkat));
        jRadioButton5.setSelected("XI".equals(tingkat));
        jRadioButton6.setSelected("XII".equals(tingkat));

        jRadioButton1.setSelected(rombel == 1);
        jRadioButton2.setSelected(rombel == 2);
        jRadioButton3.setSelected(rombel == 3);

    } else {
        JOptionPane.showMessageDialog(this, "Data siswa tidak ditemukan!");
        txtNama.setText("");
    }

    rs.close();
    pst.close();
    conn.close();

} catch (SQLException e) {
    JOptionPane.showMessageDialog(this, "Gagal mencari data: " + e.getMessage());
}
    }//GEN-LAST:event_btnCariActionPerformed

    private void tblLaporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLaporanMouseClicked

        int baris = tblLaporan.getSelectedRow();
        if (baris == -1) {
            return;
        }

        String idSiswa = model.getValueAt(baris, 0).toString();
        String nama = model.getValueAt(baris, 1).toString();
        String kelas = model.getValueAt(baris, 2).toString();
        String status = model.getValueAt(baris, 3).toString();
        String keterangan = model.getValueAt(baris, 5).toString();
        selectedId = Integer.parseInt(model.getValueAt(baris, 6).toString());

        txtId.setText(idSiswa);
        txtNama.setText(nama);
        cmbStatus.setSelectedItem(status);
        kt.setText(keterangan);
        setPilihanKelas(kelas);

        // TODO add your handling code here:
    }//GEN-LAST:event_tblLaporanMouseClicked

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed

        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
            return;
        }

        String status = cmbStatus.getSelectedItem().toString();
        String keterangan = kt.getText().trim();

        String sql = "UPDATE laporan SET status = ?, keterangan = ? WHERE id_laporan = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setString(2, keterangan);
            pst.setInt(3, selectedId);
            pst.executeUpdate();

            muatDataTabel("");
            kosongkanInput();
            JOptionPane.showMessageDialog(this, "Data laporan berhasil diedit.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengedit data: " + e.getMessage());
        }
    // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaActionPerformed

    /**
     * @param args the command line arguments
     */
    // Panggil ini jika frame laporan sudah terbuka dan data baru masuk.
    // Panggil ini jika frame laporan sudah terbuka dan data baru masuk.

public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() ->
                new laporan().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JComboBox<String> cmbKelas;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField kt;
    private javax.swing.JTable tblLaporan;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}
