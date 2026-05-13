# 1. Merge Database schemas
Get-Content 'Partie Yazidi/schema.sql', 'Partie Wajed/schema.sql' -ErrorAction SilentlyContinue | Out-File -Append 'src/main/resources/database/schema.sql' -Encoding UTF8

# 2. Migrate Models
$models = @(
    'Partie Yazidi/src/main/java/com/gestionclub/model/Materiel.java',
    'Partie Yazidi/src/main/java/com/gestionclub/model/Tache.java',
    'Partie Wajed/src/main/java/com/gestionclub/finance/model/Cotisation.java',
    'Partie Wajed/src/main/java/com/gestionclub/finance/model/Depense.java',
    'Partie Wajed/src/main/java/com/gestionclub/reunion/model/Reunion.java',
    'Partie Wajed/src/main/java/com/gestionclub/suivi/model/HistoriqueAction.java'
)

foreach ($model in $models) {
    if (Test-Path $model) {
        $dest = 'src/main/java/application/club/donnees/' + (Split-Path $model -Leaf)
        Copy-Item $model $dest
        (Get-Content $dest) -replace '^package .*', 'package application.club.donnees;' | Set-Content $dest -Encoding UTF8
    }
}

# 3. Migrate DAOs
$daos = @(
    @{ src='Partie Yazidi/src/main/java/com/gestionclub/repository/MaterielRepository.java'; dest='src/main/java/application/club/basededonnees/RequetesMateriel.java' },
    @{ src='Partie Yazidi/src/main/java/com/gestionclub/repository/TacheRepository.java'; dest='src/main/java/application/club/basededonnees/RequetesTache.java' },
    @{ src='Partie Wajed/src/main/java/com/gestionclub/finance/dao/CotisationDao.java'; dest='src/main/java/application/club/basededonnees/RequetesCotisation.java' },
    @{ src='Partie Wajed/src/main/java/com/gestionclub/finance/dao/DepenseDao.java'; dest='src/main/java/application/club/basededonnees/RequetesDepense.java' },
    @{ src='Partie Wajed/src/main/java/com/gestionclub/reunion/dao/ReunionDao.java'; dest='src/main/java/application/club/basededonnees/RequetesReunion.java' },
    @{ src='Partie Wajed/src/main/java/com/gestionclub/suivi/dao/HistoriqueActionDao.java'; dest='src/main/java/application/club/basededonnees/RequetesHistoriqueAction.java' },
    @{ src='Partie Wajed/src/main/java/com/gestionclub/suivi/dao/StatistiquesDao.java'; dest='src/main/java/application/club/basededonnees/RequetesStatistiques.java' }
)

foreach ($dao in $daos) {
    if (Test-Path $dao.src) {
        Copy-Item $dao.src $dao.dest
        $content = Get-Content $dao.dest
        
        # Replace package
        $content = $content -replace '^package .*', 'package application.club.basededonnees;'
        
        # Replace imports for Models
        $content = $content -replace 'import com\.gestionclub\.model\..*', 'import application.club.donnees.*;'
        $content = $content -replace 'import com\.gestionclub\.finance\.model\..*', 'import application.club.donnees.*;'
        $content = $content -replace 'import com\.gestionclub\.reunion\.model\..*', 'import application.club.donnees.*;'
        $content = $content -replace 'import com\.gestionclub\.suivi\.model\..*', 'import application.club.donnees.*;'
        
        # Replace class name
        $oldClassName = (Split-Path $dao.src -Leaf).Replace('.java', '')
        $newClassName = (Split-Path $dao.dest -Leaf).Replace('.java', '')
        $content = $content -replace "class $oldClassName", "class $newClassName"
        $content = $content -replace "public $oldClassName", "public $newClassName"
        
        # Fix DB connection
        $content = $content -replace 'import com\.gestionclub\.config\.DatabaseConnection;', 'import application.club.outils.ConnexionOracle;'
        $content = $content -replace 'import com\.gestionclub\.db\.DatabaseManager;', 'import application.club.outils.ConnexionOracle;'
        $content = $content -replace 'DatabaseConnection\.getConnection\(\)', 'ConnexionOracle.getConnection()'
        $content = $content -replace 'DatabaseManager\.getConnection\(\)', 'ConnexionOracle.getConnection()'
        
        $content | Set-Content $dao.dest -Encoding UTF8
    }
}
