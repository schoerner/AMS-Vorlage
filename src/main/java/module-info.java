module de.bs1bt.ams.ams {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens de.bs1bt.ams.model to javafx.fxml;
    exports de.bs1bt.ams.model;

    opens de.bs1bt.ams.mvc to javafx.fxml;
    exports de.bs1bt.ams.mvc;
}