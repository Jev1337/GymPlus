-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 07 fév. 2024 à 11:08
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12
SET
  SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

START TRANSACTION;

SET
  time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */
;

/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */
;

/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */
;

/*!40101 SET NAMES utf8mb4 */
;

--
-- Base de données : `projet_pi`
--
-- --------------------------------------------------------
--
-- Structure de la table `abonnement`
--
CREATE TABLE `abonnement` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `dateFinAb` DATE DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `commentaire`
--
CREATE TABLE `commentaire` (
  `id_comment` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `id_post` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `likes` int(11) DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `detailfacture`
--
CREATE TABLE `detailfacture` (
  `idFacture` int(11) NOT NULL,
  `idDetailFacture` int(11) NOT NULL,
  `idProduit` int(11) NOT NULL,
  `prixVenteUnitaire` float DEFAULT NULL,
  `quantite` int(11) DEFAULT NULL,
  `tauxRemise` float DEFAULT NULL,
  `prixTotalArticle` float DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `equipements_details`
--
CREATE TABLE `equipements_details` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `duree_de_vie` varchar(255) DEFAULT NULL,
  `etat` varchar(255) DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `event_details`
--
CREATE TABLE `event_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `event_date` datetime DEFAULT NULL,
  `duree` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `event_participants`
--
CREATE TABLE `event_participants` (
  `event_details_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `facture`
--
CREATE TABLE `facture` (
  `idFacture` int(11) NOT NULL,
  `dateVente` date NOT NULL DEFAULT current_timestamp(),
  `prixTatalPaye` float DEFAULT NULL,
  `methodeDePaiement` varchar(255) DEFAULT NULL,
  `id` int(11) NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `maintenances`
--
CREATE TABLE `maintenances` (
  `id` int(11) NOT NULL,
  `equipements_details_id` int(11) DEFAULT NULL,
  `date_maintenance` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `objectif`
--
CREATE TABLE `objectif` (
  `idObjectif` int(11) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `poidsObj` float DEFAULT NULL,
  `dateD` date DEFAULT NULL,
  `dateF` date DEFAULT NULL,
  `PoidsAct` float DEFAULT NULL,
  `Taille` float DEFAULT NULL,
  `Alergie` varchar(255) DEFAULT NULL,
  `TypeObj` varchar(255) DEFAULT NULL,
  `CoachId` int(11) DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `planning`
--
CREATE TABLE `planning` (
  `id_Planning` int(11) NOT NULL,
  `idObjectif` int(11) DEFAULT NULL,
  `TrainingProg` text DEFAULT NULL,
  `FoodProg` text DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `post`
--
CREATE TABLE `post` (
  `id_post` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `mode` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `photo` text DEFAULT NULL,
  `likes` int(11) DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------
--
-- Structure de la table `produit`
--
CREATE TABLE `produit` (
  `idProduit` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `prix` float DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `categorie` varchar(255) DEFAULT NULL,
  `photo` text DEFAULT NULL,
  `seuil` int(11) DEFAULT NULL,
  `promo` float DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

--
-- Déchargement des données de la table `produit`
--
INSERT INTO
  `produit` (
    `idProduit`,
    `name`,
    `prix`,
    `stock`,
    `description`,
    `categorie`,
    `photo`,
    `seuil`,
    `promo`
  )
VALUES
  (
    5,
    'produit1',
    2.5,
    23,
    'food1',
    'food',
    'azertyuiop',
    2,
    0.05
  );

-- --------------------------------------------------------
--
-- Structure de la table `user`
--
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `date_naiss` date DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `num_tel` varchar(255) DEFAULT NULL,
  `adresse` text DEFAULT NULL,
  `photo` text DEFAULT NULL,
  `faceid` varchar(255) DEFAULT NULL,
  `faceid_ts` date NOT NULL DEFAULT current_timestamp()
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

--
-- Index pour les tables déchargées
--
--
-- Index pour la table `abonnement`
--
ALTER TABLE
  `abonnement`
ADD
  PRIMARY KEY (`id`),
ADD
  KEY `user_id` (`user_id`);

--
-- Index pour la table `commentaire`
--
ALTER TABLE
  `commentaire`
ADD
  PRIMARY KEY (`id_comment`),
ADD
  KEY `user_id` (`user_id`);


--
-- Index pour la table `detailfacture`
--
ALTER TABLE
  `detailfacture`
ADD
  PRIMARY KEY (`idDetailFacture`, `idFacture`),
ADD
  KEY `FK_idProduit` (`idProduit`),
ADD
  KEY `Fk_idFacture` (`idFacture`);

--
-- Index pour la table `equipements_details`
--
ALTER TABLE
  `equipements_details`
ADD
  PRIMARY KEY (`id`);

--
--
-- Index pour la table `event_participants`
--
ALTER TABLE
  `event_participants`
ADD
  PRIMARY KEY (`event_details_id`, `user_id`),
ADD
  KEY `user_id` (`user_id`);

--
-- Index pour la table `facture`
--
ALTER TABLE
  `facture`
ADD
  PRIMARY KEY (`idFacture`),
ADD
  KEY `FK_id` (`id`);

--
-- Index pour la table `maintenances`
--
ALTER TABLE
  `maintenances`
ADD
  PRIMARY KEY (`id`),
ADD
  KEY `equipements_details_id` (`equipements_details_id`);

--
-- Index pour la table `objectif`
--
ALTER TABLE
  `objectif`
ADD
  PRIMARY KEY (`idObjectif`),
ADD
  KEY `userId` (`userId`),
ADD
  KEY `coachId` (`CoachId`);

--
-- Index pour la table `planning`
--
ALTER TABLE
  `planning`
ADD
  PRIMARY KEY (`id_Planning`),
ADD
  KEY `idObjectif` (`idObjectif`);

--
-- Index pour la table `post`
--
ALTER TABLE
  `post`
ADD
  PRIMARY KEY (`id_post`),
ADD
  KEY `user_id` (`user_id`);

--
-- Index pour la table `produit`
--
ALTER TABLE
  `produit`
ADD
  PRIMARY KEY (`idProduit`);

--
-- Index pour la table `user`
--
ALTER TABLE
  `user`
ADD
  PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--
--
-- AUTO_INCREMENT pour la table `facture`
--
ALTER TABLE
  `facture`
MODIFY
  `idFacture` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `abonnement`
--
ALTER TABLE `abonnement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE
  `produit`
MODIFY
  `idProduit` int(11) NOT NULL AUTO_INCREMENT,
  AUTO_INCREMENT = 7;

--
-- Contraintes pour les tables déchargées
--
--
-- Contraintes pour la table `abonnement`
--
ALTER TABLE
  `abonnement`
ADD
  CONSTRAINT `abonnement_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `commentaire`
--
ALTER TABLE
  `commentaire`
ADD
  CONSTRAINT `commentaire_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
ADD
  CONSTRAINT `commentaire_ibfk_2` FOREIGN KEY (`id_post`) REFERENCES `post` (`id_post`);

--
-- Contraintes pour la table `detailfacture`
--
ALTER TABLE
  `detailfacture`
ADD
  CONSTRAINT `FK_idProduit` FOREIGN KEY (`idProduit`) REFERENCES `produit` (`idProduit`),
ADD
  CONSTRAINT `Fk_idFacture` FOREIGN KEY (`idFacture`) REFERENCES `facture` (`idFacture`);

--
-- Contraintes pour la table `event_participants`
--
ALTER TABLE
  `event_participants`
ADD
  CONSTRAINT `event_participants_ibfk_1` FOREIGN KEY (`event_details_id`) REFERENCES `event_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD
  CONSTRAINT `event_participants_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `facture`
--
ALTER TABLE
  `facture`
ADD
  CONSTRAINT `FK_id` FOREIGN KEY (`id`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `maintenances`
--
ALTER TABLE
  `maintenances`
ADD
  CONSTRAINT `maintenances_ibfk_1` FOREIGN KEY (`equipements_details_id`) REFERENCES `equipements_details` (`id`);

--
-- Contraintes pour la table `objectif`
--
ALTER TABLE
  `objectif`
ADD
  CONSTRAINT `objectif_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
ADD
  CONSTRAINT `objectif_ibfk_2` FOREIGN KEY (`coachId`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `planning`
--
ALTER TABLE
  `planning`
ADD
  CONSTRAINT `planning_ibfk_1` FOREIGN KEY (`idObjectif`) REFERENCES `objectif` (`idObjectif`);

--
-- Contraintes pour la table `post`
--
ALTER TABLE
  `post`
ADD
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */
;

/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */
;

/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */
;
