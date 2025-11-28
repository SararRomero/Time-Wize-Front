package com.example.timewize.iu.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import com.example.timewize.R;

public class UserReportsActivity extends AppCompatActivity {

    private Spinner spinnerTimeRange, spinnerUserType;
    private TextView tvActiveUsers, tvAvgTime, tvTotalSessions;
    private TextView tvStudyTime, tvWorkTime, tvOtherTime;
    private TextView btnBack, btnExport; // Cambiado de ImageView a TextView

    private List<ReportData> reportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reports);

        initViews();
        setupSpinners();
        setupClickListeners();
        setupNavbarListeners();
        loadReportData();
        updateUI();
    }

    private void initViews() {
        spinnerTimeRange = findViewById(R.id.spinnerTimeRange);
        spinnerUserType = findViewById(R.id.spinnerUserType);
        tvActiveUsers = findViewById(R.id.tvActiveUsers);
        tvAvgTime = findViewById(R.id.tvAvgTime);
        tvTotalSessions = findViewById(R.id.tvTotalSessions);
        tvStudyTime = findViewById(R.id.tvStudyTime);
        tvWorkTime = findViewById(R.id.tvWorkTime);
        tvOtherTime = findViewById(R.id.tvOtherTime);
        btnBack = findViewById(R.id.btnBack); // Ahora es TextView
        btnExport = findViewById(R.id.btnExport); // Ahora es TextView
    }

    private void setupSpinners() {
        // Configurar spinner de rango de tiempo
        String[] timeRanges = new String[]{
                "Última semana",
                "Último mes",
                "Últimos 3 meses",
                "Último año"
        };

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                timeRanges
        );
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeRange.setAdapter(timeAdapter);

        // Configurar spinner de tipo de usuario
        String[] userTypes = new String[]{
                "Todos los usuarios",
                "Estudiantes",
                "Profesionales",
                "Premium",
                "Básico"
        };

        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                userTypes
        );
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(userAdapter);

        // Listeners para los spinners
        spinnerTimeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterReportData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterReportData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnExport.setOnClickListener(v -> exportReport());
    }

    private void setupNavbarListeners() {
        // Buscar las vistas del navbar
        ViewGroup navbarBottom = findViewById(R.id.navbar_bottomm);

        if (navbarBottom == null) {
            Toast.makeText(this, "Navbar no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buscar los botones del navbar (ajusta los IDs según tu layout de navbar_bottomm.xml)
        ImageView navAdminHome = navbarBottom.findViewById(R.id.navAdminHome);
        ImageView navCategories = navbarBottom.findViewById(R.id.navCategories);
        ImageView navReports = navbarBottom.findViewById(R.id.navReports);
        ImageView navAdminProfile = navbarBottom.findViewById(R.id.navAdminProfile);
        ImageButton navAddAdmin = navbarBottom.findViewById(R.id.navAddAdmin);

        // Verificar que los botones existen
        if (navAdminHome == null) {
            Toast.makeText(this, "Botones del navbar no encontrados", Toast.LENGTH_SHORT).show();
            return;
        }

        navAdminHome.setOnClickListener(v -> {
            // Navegar al Dashboard Admin
            try {
                Intent intent = new Intent(UserReportsActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(UserReportsActivity.this, "Error al navegar al Inicio", Toast.LENGTH_SHORT).show();
                Log.e("UserReportsActivity", "Error navegando a AdminDashboard", e);
            }
        });

        navCategories.setOnClickListener(v -> {
            // Navegar a Categorías
            try {
                Intent intent = new Intent(UserReportsActivity.this, AdminCategoriesActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(UserReportsActivity.this, "Pantalla de Categorías no disponible", Toast.LENGTH_SHORT).show();
                Log.e("UserReportsActivity", "Error navegando a AdminCategories", e);
            }
        });

        navReports.setOnClickListener(v -> {
            // Ya estamos en Reportes
            Toast.makeText(UserReportsActivity.this, "Ya estás en Reportes", Toast.LENGTH_SHORT).show();
        });

        navAdminProfile.setOnClickListener(v -> {
            Toast.makeText(UserReportsActivity.this, "Perfil Admin - En desarrollo", Toast.LENGTH_SHORT).show();
        });

        navAddAdmin.setOnClickListener(v -> {
            // Navegar a la pantalla de usuarios
            try {
                Intent intent = new Intent(UserReportsActivity.this, AdminUsersActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(UserReportsActivity.this, "Error al navegar a Usuarios", Toast.LENGTH_SHORT).show();
                Log.e("UserReportsActivity", "Error navegando a AdminUsers", e);
            }
        });
    }

    private void loadReportData() {
        // Aquí cargarías los datos reales de tu base de datos o API
        reportData = new ArrayList<>();

        // Datos de ejemplo
        ReportData report = new ReportData();
        report.activeUsers = 1250;
        report.averageTime = 3.2;
        report.totalSessions = 4580;
        report.studyTime = 12.4;
        report.workTime = 8.2;
        report.otherTime = 3.4;

        reportData.add(report);
    }

    private void filterReportData() {
        String timeRange = spinnerTimeRange.getSelectedItem().toString();
        String userType = spinnerUserType.getSelectedItem().toString();

        // Aquí implementarías la lógica de filtrado según los criterios seleccionados
        // Por ahora solo actualizamos la UI con los datos existentes
        updateUI();

        // Mostrar qué filtros se aplicaron
        Toast.makeText(this,
                "Filtros: " + timeRange + " - " + userType,
                Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {
        if (reportData != null && !reportData.isEmpty()) {
            ReportData currentReport = reportData.get(0);

            tvActiveUsers.setText(String.format("%,d", currentReport.activeUsers));
            tvAvgTime.setText(String.format("%.1fh", currentReport.averageTime));
            tvTotalSessions.setText(String.format("%,d", currentReport.totalSessions));
            tvStudyTime.setText(String.format("%.1fh", currentReport.studyTime));
            tvWorkTime.setText(String.format("%.1fh", currentReport.workTime));
            tvOtherTime.setText(String.format("%.1fh", currentReport.otherTime));
        }
    }

    private void exportReport() {
        // Implementar la funcionalidad de exportación
        Toast.makeText(this, "Exportando reportes...", Toast.LENGTH_SHORT).show();
    }

    // Clase interna simple para los datos del reporte
    private static class ReportData {
        public int activeUsers;
        public double averageTime;
        public int totalSessions;
        public double studyTime;
        public double workTime;
        public double otherTime;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refrescar datos cuando la actividad se reanuda
        loadReportData();
        updateUI();
    }
}