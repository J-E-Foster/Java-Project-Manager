-- MySQL dump 10.13  Distrib 8.0.25, for Win64 (x86_64)
--
-- Host: localhost    Database: poisepms
-- ------------------------------------------------------
-- Server version	8.0.25

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `architect`
--

DROP TABLE IF EXISTS `architect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `architect` (
  `Surname` varchar(255) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `CellNum` varchar(255) DEFAULT NULL,
  `EmailAddress` varchar(255) DEFAULT NULL,
  `PhysAddress` varchar(255) DEFAULT NULL,
  `Type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Surname`,`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `architect`
--

LOCK TABLES `architect` WRITE;
/*!40000 ALTER TABLE `architect` DISABLE KEYS */;
INSERT INTO `architect` VALUES ('Van der Merwe','Koos','0612234444','koosdiedoos@gmail.com','23 Zolpidemsingel, Pretoria, 0001','Architect'),('Yopli','Dopli','0111111111','yop_dop_lee@gmail.com','115 Seekatonkels, 16 Kusweg, Strand, 7140','Architect');
/*!40000 ALTER TABLE `architect` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contractor`
--

DROP TABLE IF EXISTS `contractor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contractor` (
  `Surname` varchar(255) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `CellNum` varchar(255) DEFAULT NULL,
  `EmailAddress` varchar(255) DEFAULT NULL,
  `PhysAddress` varchar(255) DEFAULT NULL,
  `Type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Surname`,`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contractor`
--

LOCK TABLES `contractor` WRITE;
/*!40000 ALTER TABLE `contractor` DISABLE KEYS */;
INSERT INTO `contractor` VALUES ('De la Le-Rich','Lize-Marie','0645536476','lizzelippe@gmail.com','32 Sysislette Rylaan, Pretoria, 0001','Contractor'),('Dompfman','Rolfof','0874637878','dompfman@hotmail.com','11 Kromhout Straat, Gonnamanskraalbaai, 2343','Contractor'),('Van der Vyfer','Egbert','0879998798','eggie.vatvyf@gmail.com','776 Neutropion Heights, Technopark, 7600','Contractor');
/*!40000 ALTER TABLE `contractor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `Surname` varchar(255) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `CellNum` varchar(255) DEFAULT NULL,
  `EmailAddress` varchar(255) DEFAULT NULL,
  `PhysAddress` varchar(255) DEFAULT NULL,
  `Type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Surname`,`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('Grompf','Dompf','0716665654','dompfgrompf@gmail.com','35 Dolcinea Avenue, Domfpfrompolis, 3221','Customer'),('Marais','Hendriko','0228783323','marais_h@webmail.com','23 Maraisstraat, Stellenbosch, 7600','Customer'),('Van der Kolf','Januk','0989997876','januk.vd@gmail.com','23 Zombotia Rylaansingel, Klapmuts, 7625','Customer');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `ProjectNum` varchar(255) NOT NULL,
  `ProjectName` varchar(255) NOT NULL,
  `BuildType` varchar(255) DEFAULT NULL,
  `PhysAddress` varchar(255) DEFAULT NULL,
  `ErfNum` varchar(255) DEFAULT NULL,
  `TotalFee` varchar(255) DEFAULT NULL,
  `TotalPaid` varchar(255) DEFAULT NULL,
  `DeadLine` varchar(255) DEFAULT NULL,
  `Architect` varchar(255) DEFAULT NULL,
  `Contractor` varchar(255) DEFAULT NULL,
  `Customer` varchar(255) DEFAULT NULL,
  `StructuralEng` varchar(255) DEFAULT NULL,
  `ProjectMan` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ProjectNum`,`ProjectName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES ('1111','MKUltra','Apartment','86 Lekke Street, Paarl, 7620','1341300','100000000','500','17 Mar 2022','Van der Merwe, Koos','Van der Vyfer, Egbert','Van der Kolf, Januk','Van Schalkwyk, Nico','Van Deventer, Nicola'),('1112','Diogenes','House','122 Kruikvatrylaansingel, Arktarog Gebergtes, Mesopotamia, 2213','11220009','200.23','105.24','15 Jan 2022','Van der Merwe, Koos','De la Le-Rich, Lize-Marie','Marais, Hendriko','Brandt, Rikus','McGwaddy, Paddy'),('1113','Ploppy','Apartment','13 Generaal Labuschagne Rylaan Pad, Bellville, 7493','1212133','5000000000','100.77','13 Jul 2021','Yopli, Dopli','Dompfman, Rolfof','Grompf, Dompf','Bolckman, Dirk','De la van Schalkwyk-Nel, Nadia');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectman`
--

DROP TABLE IF EXISTS `projectman`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projectman` (
  `Surname` varchar(255) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `CellNum` varchar(255) DEFAULT NULL,
  `EmailAddress` varchar(255) DEFAULT NULL,
  `PhysAddress` varchar(255) DEFAULT NULL,
  `Type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Surname`,`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectman`
--

LOCK TABLES `projectman` WRITE;
/*!40000 ALTER TABLE `projectman` DISABLE KEYS */;
INSERT INTO `projectman` VALUES ('De la van Schalkwyk-Nel','Nadia','0765554543','vanschalkwyk.nel@nadia.co.za','87 Dertigjarigetanniestraat, Durbanville, 7490','Project Manager'),('McGwaddy','Paddy','0564764847','patrick_mac@webmail.com','45 Cirrhosis Avenue, Dublin, Scotland, Australia, 8998','Project Manager'),('Van Deventer','Nicola','0746635546','nikki_van_dev@gmail.com','54 Northern Lights Heights, 420 Dank Street, Dankville, 9983','Project Manager');
/*!40000 ALTER TABLE `projectman` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `structuraleng`
--

DROP TABLE IF EXISTS `structuraleng`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `structuraleng` (
  `Surname` varchar(255) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `CellNum` varchar(255) DEFAULT NULL,
  `EmailAddress` varchar(255) DEFAULT NULL,
  `PhysAddress` varchar(255) DEFAULT NULL,
  `Type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Surname`,`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `structuraleng`
--

LOCK TABLES `structuraleng` WRITE;
/*!40000 ALTER TABLE `structuraleng` DISABLE KEYS */;
INSERT INTO `structuraleng` VALUES ('Bolckman','Dirk','0986665432','dirk.die.urk@gmail.com','33 Lemoenhout Straat, Eufesiabaai, 9099','Structural Engineer'),('Brandt','Rikus','0764473674','vuurspeel@mweb.co.zo','34 Kremetarthout Singel, Tergnietbosrivier, 3231','Structural Engineer'),('Van Schalkwyk','Nico','0989998978','nikky.niksie@gmail.com','114 Spinhomenekkoophom Road, Welgemoed, 7538','Structural Engineer');
/*!40000 ALTER TABLE `structuraleng` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-16 14:06:50
