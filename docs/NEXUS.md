````md
# 🚀 COMPLETE NEXUS SETUP (Step-by-Step)

Perfect ✅  
Here are **Nexus steps in one place**, clean and simple.

---

## ✅ STEP 1 — Start Nexus (Docker)

```bash
docker run -d --name nexus -p 8081:8081 sonatype/nexus3
```
````

Open:

```text
http://localhost:8081
```

---

## ✅ STEP 2 — Login

Username:

```text
admin
```

Get password:

```bash
docker exec -it nexus cat /nexus-data/admin.password
```

Login → Change password.

---

## ✅ STEP 3 — Create Repositories

Go to:

```text
Repositories → Create repository
```

### Create Release Repo

- Type: `maven2 (hosted)`
- Name: `maven-bus`
- Version Policy: `Release`
- Deployment Policy: `Allow redeploy`
- Create

---

### Create Snapshot Repo

- Type: `maven2 (hosted)`
- Name: `maven-snapshots`
- Version Policy: `Snapshot`
- Deployment Policy: `Allow redeploy`
- Create

---

## ✅ STEP 4 — Update `pom.xml`

Add this inside `<project>`:

```xml
<distributionManagement>
  <repository>
    <id>nexus-releases</id>
    <url>http://localhost:8081/repository/maven-bus/</url>
  </repository>

  <snapshotRepository>
    <id>nexus-snapshots</id>
    <url>http://localhost:8081/repository/maven-snapshots/</url>
  </snapshotRepository>
</distributionManagement>
```

---

## ✅ STEP 5 — Create `settings.xml`

Create file:

```bash
nano ~/.m2/settings.xml
```

Paste:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">

  <servers>
    <server>
      <id>nexus-releases</id>
      <username>admin</username>
      <password>YOUR_PASSWORD</password>
    </server>

    <server>
      <id>nexus-snapshots</id>
      <username>admin</username>
      <password>YOUR_PASSWORD</password>
    </server>
  </servers>

</settings>
```

Replace password. Save.

---

## ✅ STEP 6 — Deploy SNAPSHOT Version

If version is:

```xml
<version>0.0.1-SNAPSHOT</version>
```

Run:

```bash
mvn clean deploy
```

Check in:

```text
Browse → maven-snapshots
```

---

## ✅ STEP 7 — Deploy RELEASE Version

Change version:

```xml
<version>1.0.0</version>
```

Run:

```bash
mvn clean deploy
```

Check in:

```text
Browse → maven-bus
```

---

# 🎯 DONE

You now have:

- Snapshot repository (dev builds)
- Release repository (stable builds)
- Versioned artifact storage
- Enterprise-ready deployment flow

---

## If you want next:

- How to integrate Nexus in CI/CD
- Or how companies use Nexus in real pipeline
- Or how to explain Nexus in interview

```

If you want, tell me **your GitHub Actions CI/CD plan** (build/test + sonar + deploy to nexus), and I’ll give the exact `yaml` next.
```
