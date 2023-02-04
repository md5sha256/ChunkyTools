plugins {
    java
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

group = "io.github.md5sha256"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "spigot-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        content {
            includeGroup("org.bukkit")
            includeGroup("org.spigotmc")
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    implementation("io.papermc:paperlib:1.0.8")
    implementation("cloud.commandframework", "cloud-paper", "1.8.0")
    implementation("cloud.commandframework", "cloud-minecraft-extras", "1.8.0")
    implementation("com.github.md5sha256:SpigotUtils:da4c09c984")
}

var targetJavaVersion = 17
java.toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))

tasks {

    assemble {
        dependsOn(shadowJar)
    }

    compileJava {
        options.release.set(targetJavaVersion)
    }

    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        val base = "io.github.md5sha256.chunkytools.libraries"
        relocate("cloud.commandframework", "${base}.cloud.commandframework")
        relocate("io.papermc.lib", "${base}.papermc.lib")
        relocate("com.github.md5sha256.spigotutils", "${base}.spigotutils")
        relocate("com.google", "${base}.google")
        relocate("io.leangen", "${base}.leangen")
        relocate("javax", "${base}.javax")
        relocate("net.kyori", "${base}.kyori")
        relocate("org.aopalliance", "${base}.aopalliance")
        relocate("org.checkerframework", "${base}.checkerframework")
    }

}

