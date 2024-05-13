-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : lun. 13 mai 2024 à 20:59
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gymplus`
--

-- --------------------------------------------------------

--
-- Structure de la table `abonnement`
--

CREATE TABLE `abonnement` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `dateFinAb` date DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `abonnement`
--

INSERT INTO `abonnement` (`id`, `user_id`, `dateFinAb`, `type`) VALUES
(1, 13371337, '2025-05-13', 'GP 3'),
(2, 19191919, '2024-11-13', 'GP 2');

-- --------------------------------------------------------

--
-- Structure de la table `abonnement_details`
--

CREATE TABLE `abonnement_details` (
  `name` varchar(255) NOT NULL,
  `prix` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `abonnement_details`
--

INSERT INTO `abonnement_details` (`name`, `prix`) VALUES
('GP 1', 29.99),
('GP 2', 49.99),
('GP 3', 69.99);

-- --------------------------------------------------------

--
-- Structure de la table `black_listed`
--

CREATE TABLE `black_listed` (
  `id_user` int(11) NOT NULL,
  `start_ban` date NOT NULL DEFAULT current_timestamp(),
  `end_ban` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `commentaire`
--

INSERT INTO `commentaire` (`id_comment`, `user_id`, `id_post`, `content`, `date`, `likes`) VALUES
(10, 13371337, 40, 'cool pic', '2024-05-13', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `complains`
--

CREATE TABLE `complains` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `feedback` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `complains`
--

INSERT INTO `complains` (`id`, `user_id`, `post_id`, `feedback`) VALUES
(1, 13371337, 43, 'bad post');

-- --------------------------------------------------------

--
-- Structure de la table `conversation`
--

CREATE TABLE `conversation` (
  `id` int(11) NOT NULL,
  `last_message_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `equipements_details`
--

INSERT INTO `equipements_details` (`id`, `name`, `description`, `duree_de_vie`, `etat`) VALUES
(1, '2 KG Dumbbell', 'Dumbbell 1', '2', 'Good'),
(2, '4 KG Dumbbell', 'Dumbbell 2', '2', 'Good'),
(3, '6 KG Dumbbell', 'Dumbbell 3', '2', 'Bad'),
(4, '8 KG Dumbbell', 'Dumbbell 4', '2', 'Good'),
(5, '10 KG Dumbbell', 'Dumbbell 5', '2', 'Under Maintenance'),
(6, '12 KG Dumbbell', 'Dumbbell 6', '2', 'Good'),
(7, '14 KG Dumbbell', 'Dumbbell 7', '2', 'Good'),
(8, '16 KG Dumbbell', 'Dumbbell 8', '2', 'Bad'),
(9, '20 KG Dumbbell', 'Dumbbell 9', '2', 'Good'),
(10, '22 KG Dumbbell', 'Dumbbell 10', '2', 'Good'),
(11, '20 KG Barbell', 'Barbell 1', '2', 'Good'),
(12, '10 KG Barbell', 'Barbell 2', '2', 'Under Maintenance'),
(13, '10 KG Z-BAR Barbell', 'Barbell 3', '2', 'Good');

-- --------------------------------------------------------

--
-- Structure de la table `event_details`
--

CREATE TABLE `event_details` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `event_date` datetime DEFAULT NULL,
  `duree` varchar(255) DEFAULT NULL,
  `nb_places` int(11) DEFAULT NULL,
  `nb_total` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `event_participants`
--

CREATE TABLE `event_participants` (
  `event_details_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `rate` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `livraison`
--

CREATE TABLE `livraison` (
  `idLivraison` int(11) NOT NULL,
  `idFacture` int(11) DEFAULT NULL,
  `idClient` int(11) DEFAULT NULL,
  `Lieu` varchar(255) DEFAULT NULL,
  `etat` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `maintenances`
--

CREATE TABLE `maintenances` (
  `id` int(11) NOT NULL,
  `equipements_details_id` int(11) NOT NULL,
  `date_maintenance` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `maintenances`
--

INSERT INTO `maintenances` (`id`, `equipements_details_id`, `date_maintenance`, `status`) VALUES
(1, 12, '2024-05-02', 'Almost Done'),
(2, 5, '2024-05-09', 'Just Started');

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `conversation_id` int(11) NOT NULL,
  `content` text NOT NULL,
  `createdAt` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `notif`
--

CREATE TABLE `notif` (
  `id` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `etat` varchar(255) DEFAULT NULL,
  `etat_commentaire` varchar(255) DEFAULT NULL,
  `titre` varchar(255) DEFAULT NULL,
  `nomUser` varchar(255) DEFAULT NULL,
  `idProduit` int(11) DEFAULT NULL,
  `datevente` date DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `participantmessanger`
--

CREATE TABLE `participantmessanger` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `conversation_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `planning`
--

CREATE TABLE `planning` (
  `id_Planning` int(11) NOT NULL,
  `idObjectif` int(11) DEFAULT NULL,
  `TrainingProg` text DEFAULT NULL,
  `FoodProg` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `likes` int(11) DEFAULT NULL,
  `nbComnts` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `post`
--

INSERT INTO `post` (`id_post`, `user_id`, `mode`, `content`, `date`, `photo`, `likes`, `nbComnts`) VALUES
(40, 12340000, NULL, 'hello', '2024-05-13', 'USERIMG40.jpg', 1, 1),
(43, 12340000, NULL, '**** you', '2024-05-13', '', 0, 0),
(44, 13371337, NULL, 'new pic', '2024-05-13', 'USERIMG.jpg', 0, 0);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `event_points` int(11) DEFAULT NULL,
  `faceid` varchar(255) DEFAULT NULL,
  `faceid_ts` date DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `username`, `firstname`, `lastname`, `date_naiss`, `password`, `email`, `role`, `num_tel`, `adresse`, `photo`, `event_points`, `faceid`, `faceid_ts`) VALUES
(12340000, 'FaroukChb53', 'Farouk', 'Chbichib', '2024-02-09', '$2b$10$gSdsptgM8hfCuK0Fc8g8Ye2rzZHUJtO0QJAwkIIJcjaWrxGJLTyJy', 'mrtngamer53@gmail.com', 'client', '12340000', 'Ben Arous', 'USERIMG12340000.jpg', 5000, '', '2024-04-24'),
(12345632, 'aaaa', 'aaaa', 'aaaa', '2024-02-09', '$2b$10$gSdsptgM8hfCuK0Fc8g8Ye2rzZHUJtO0QJAwkIIJcjaWrxGJLTyJy', 'aa@aa.aa', 'admin', '22335566', 'aaaa', 'USERIMG12345632.jpg', 600, '', '2024-02-24'),
(13371337, 'malekamir34', 'AbdelMalek', 'Amir', '2024-02-09', '$2b$10$gSdsptgM8hfCuK0Fc8g8Ye2rzZHUJtO0QJAwkIIJcjaWrxGJLTyJy', 'malekamir34@yahoo.com', 'client', '13371337', 'Ennasr', 'USERIMG13371337.jpg', 600, '', '2024-04-24'),
(14509889, 'malekamir56', 'Malek', 'Amir', '2024-02-09', '$2b$10$gSdsptgM8hfCuK0Fc8g8Ye2rzZHUJtO0QJAwkIIJcjaWrxGJLTyJy', 'malekamir56@gmail.com', 'admin', '52920276', 'Riadh el Andalous', 'USERIMG14509889.jpg', 600, 'c8115ae846844aaa9e53ab84fc0e6ebe', '2024-05-13'),
(17171717, 'Yasmine1337', 'Yasmine', 'Ayadi', '2024-02-09', '$2b$10$gSdsptgM8hfCuK0Fc8g8Ye2rzZHUJtO0QJAwkIIJcjaWrxGJLTyJy', 'yasminetools@gmail.com', 'staff', '17171717', 'Ennasr', 'USERIMG17171717.jpg', 600, '', '2024-04-24'),
(19191919, 'donaldtr', 'Donald', 'Trump', '2024-02-09', '$2b$10$gSdsptgM8hfCuK0Fc8g8Ye2rzZHUJtO0QJAwkIIJcjaWrxGJLTyJy', 'donaldtrumpp78@gmail.com', 'client', '19191919', 'USA', 'USERIMG19191919.jpg', 600, '', '2024-04-24'),
(87654321, 'Farou', 'Fares', 'Ben Ammar', '2024-02-09', '$2b$10$gSdsptgM8hfCuK0Fc8g8Ye2rzZHUJtO0QJAwkIIJcjaWrxGJLTyJy', 'benammar.fares@esprit.tn', 'client', '87654321', 'La Marsa', 'USERIMG87654321.jpg', 600, '', '2024-04-24');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `abonnement`
--
ALTER TABLE `abonnement`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `type` (`type`);

--
-- Index pour la table `abonnement_details`
--
ALTER TABLE `abonnement_details`
  ADD PRIMARY KEY (`name`);

--
-- Index pour la table `commentaire`
--
ALTER TABLE `commentaire`
  ADD PRIMARY KEY (`id_comment`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `commentaire_ibfk_2` (`id_post`);

--
-- Index pour la table `complains`
--
ALTER TABLE `complains`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `post_id` (`post_id`);

--
-- Index pour la table `conversation`
--
ALTER TABLE `conversation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `last_message_id_index` (`last_message_id`);

--
-- Index pour la table `detailfacture`
--
ALTER TABLE `detailfacture`
  ADD PRIMARY KEY (`idDetailFacture`,`idFacture`),
  ADD KEY `FK_idProduit` (`idProduit`),
  ADD KEY `Fk_idFacture` (`idFacture`);

--
-- Index pour la table `equipements_details`
--
ALTER TABLE `equipements_details`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `event_details`
--
ALTER TABLE `event_details`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `event_participants`
--
ALTER TABLE `event_participants`
  ADD PRIMARY KEY (`event_details_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Index pour la table `facture`
--
ALTER TABLE `facture`
  ADD PRIMARY KEY (`idFacture`),
  ADD KEY `FK_id` (`id`);

--
-- Index pour la table `livraison`
--
ALTER TABLE `livraison`
  ADD PRIMARY KEY (`idLivraison`);

--
-- Index pour la table `maintenances`
--
ALTER TABLE `maintenances`
  ADD PRIMARY KEY (`id`),
  ADD KEY `equipements_details_id` (`equipements_details_id`);

--
-- Index pour la table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `conversation_id` (`conversation_id`);

--
-- Index pour la table `notif`
--
ALTER TABLE `notif`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `objectif`
--
ALTER TABLE `objectif`
  ADD PRIMARY KEY (`idObjectif`),
  ADD KEY `userId` (`userId`),
  ADD KEY `coachId` (`CoachId`);

--
-- Index pour la table `participantmessanger`
--
ALTER TABLE `participantmessanger`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `conversation_id` (`conversation_id`);

--
-- Index pour la table `planning`
--
ALTER TABLE `planning`
  ADD PRIMARY KEY (`id_Planning`),
  ADD KEY `idObjectif` (`idObjectif`);

--
-- Index pour la table `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`id_post`),
  ADD KEY `user_id` (`user_id`);

--
-- Index pour la table `produit`
--
ALTER TABLE `produit`
  ADD PRIMARY KEY (`idProduit`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `num_tel` (`num_tel`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `abonnement`
--
ALTER TABLE `abonnement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `commentaire`
--
ALTER TABLE `commentaire`
  MODIFY `id_comment` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `complains`
--
ALTER TABLE `complains`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `conversation`
--
ALTER TABLE `conversation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `equipements_details`
--
ALTER TABLE `equipements_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT pour la table `event_details`
--
ALTER TABLE `event_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `facture`
--
ALTER TABLE `facture`
  MODIFY `idFacture` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `livraison`
--
ALTER TABLE `livraison`
  MODIFY `idLivraison` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `maintenances`
--
ALTER TABLE `maintenances`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `message`
--
ALTER TABLE `message`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `notif`
--
ALTER TABLE `notif`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `objectif`
--
ALTER TABLE `objectif`
  MODIFY `idObjectif` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `participantmessanger`
--
ALTER TABLE `participantmessanger`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `planning`
--
ALTER TABLE `planning`
  MODIFY `id_Planning` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `post`
--
ALTER TABLE `post`
  MODIFY `id_post` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE `produit`
  MODIFY `idProduit` int(11) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `abonnement`
--
ALTER TABLE `abonnement`
  ADD CONSTRAINT `abonnement_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `abonnement_ibfk_2` FOREIGN KEY (`type`) REFERENCES `abonnement_details` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `commentaire`
--
ALTER TABLE `commentaire`
  ADD CONSTRAINT `commentaire_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `commentaire_ibfk_2` FOREIGN KEY (`id_post`) REFERENCES `post` (`id_post`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `detailfacture`
--
ALTER TABLE `detailfacture`
  ADD CONSTRAINT `FK_idProduit` FOREIGN KEY (`idProduit`) REFERENCES `produit` (`idProduit`),
  ADD CONSTRAINT `Fk_idFacture` FOREIGN KEY (`idFacture`) REFERENCES `facture` (`idFacture`);

--
-- Contraintes pour la table `event_participants`
--
ALTER TABLE `event_participants`
  ADD CONSTRAINT `event_participants_ibfk_1` FOREIGN KEY (`event_details_id`) REFERENCES `event_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `event_participants_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `facture`
--
ALTER TABLE `facture`
  ADD CONSTRAINT `FK_id` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `maintenances`
--
ALTER TABLE `maintenances`
  ADD CONSTRAINT `maintenances_ibfk_1` FOREIGN KEY (`equipements_details_id`) REFERENCES `equipements_details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `objectif`
--
ALTER TABLE `objectif`
  ADD CONSTRAINT `objectif_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `objectif_ibfk_2` FOREIGN KEY (`CoachId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `planning`
--
ALTER TABLE `planning`
  ADD CONSTRAINT `planning_ibfk_1` FOREIGN KEY (`idObjectif`) REFERENCES `objectif` (`idObjectif`);

--
-- Contraintes pour la table `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
