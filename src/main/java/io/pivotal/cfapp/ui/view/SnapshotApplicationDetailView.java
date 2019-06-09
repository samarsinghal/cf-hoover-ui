package io.pivotal.cfapp.ui.view;

import static io.pivotal.cfapp.ui.view.SnapshotApplicationDetailView.NAV;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import io.pivotal.cfapp.domain.AppDetail;
import io.pivotal.cfapp.repository.MetricCache;
import io.pivotal.cfapp.ui.MainLayout;
import io.pivotal.cfapp.ui.component.GridTile;

@Route(value = NAV, layout = MainLayout.class)
public class SnapshotApplicationDetailView extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    public static final String NAV = "snapshot/detail/ai";

    @Autowired
    public SnapshotApplicationDetailView(
        MetricCache cache) {
        // TODO Resource bundle for title and tile labels
        H2 title = new H2("Snapshot » Detail » AI");
        HorizontalLayout firstRow = new HorizontalLayout();
        GridTile<AppDetail> tile = new GridTile<>("Applications", buildGrid(cache.getSnapshotDetail().getApplications()));
        firstRow.add(tile);
        add(title, firstRow);
        firstRow.setSizeFull();
        setSizeFull();
    }

    private Grid<AppDetail> buildGrid(Collection<AppDetail> items) {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(2);

        Grid<AppDetail> grid = new Grid<>(AppDetail.class, false);
        ListDataProvider<AppDetail> dataProvider = new ListDataProvider<>(items);
        grid.setDataProvider(dataProvider);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

        Column<AppDetail> foundationColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.foundation]]").withProperty("foundation", AppDetail::getFoundation)).setHeader("Foundation").setTextAlign(ColumnTextAlign.CENTER).setResizable(true);
        Column<AppDetail> organizationColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.organization]]").withProperty("organization", AppDetail::getOrganization)).setHeader("Organization").setResizable(true);
        Column<AppDetail> spaceColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.space]]").withProperty("space", AppDetail::getSpace)).setHeader("Space").setResizable(true);
        Column<AppDetail> appIdColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.appId]]").withProperty("appId", AppDetail::getAppId)).setHeader("Application Id").setResizable(true);
        Column<AppDetail> appNameColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.appName]]").withProperty("appName", AppDetail::getAppName)).setHeader("Application Name").setResizable(true);
        Column<AppDetail> buildpackColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.buildpack]]").withProperty("buildpack", AppDetail::getBuildpack)).setHeader("Buildpack").setResizable(true);
        Column<AppDetail> imageColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.image]]").withProperty("image", AppDetail::getImage)).setHeader("Docker Image").setResizable(true);
        Column<AppDetail> stackColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.stack]]").withProperty("stack", AppDetail::getStack)).setHeader("Stack").setResizable(true);
        Column<AppDetail> runningInstancesColumn = grid.addColumn(new NumberRenderer<>(AppDetail::getRunningInstances, formatter)).setHeader("Running Instances").setTextAlign(ColumnTextAlign.END).setResizable(true);
        Column<AppDetail> totalInstancesColumn = grid.addColumn(new NumberRenderer<>(AppDetail::getTotalInstances, formatter)).setHeader("Total Instances").setTextAlign(ColumnTextAlign.END).setResizable(true);
        Column<AppDetail> memUsedColumn = grid.addColumn(new NumberRenderer<>(AppDetail::getMemoryUsageInGb, formatter)).setHeader("Memory Usage (in Gb)").setTextAlign(ColumnTextAlign.END).setResizable(true);
        Column<AppDetail> diskUsedColumn = grid.addColumn(new NumberRenderer<>(AppDetail::getDiskUsageInGb, formatter)).setHeader("Disk Usage (in Gb)").setTextAlign(ColumnTextAlign.END).setResizable(true);
        Column<AppDetail> urlsColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.urls]]").withProperty("urls", AppDetail::getUrlsAsCsv)).setHeader("Routes").setResizable(true);
        Column<AppDetail> lastPushedColumn = grid.addColumn(new LocalDateTimeRenderer<AppDetail>(AppDetail::getLastPushed, dateTimeFormatter)).setHeader("Last Pushed").setTextAlign(ColumnTextAlign.END).setResizable(true);
        Column<AppDetail> lastEventColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.lastEvent]]").withProperty("lastEvent", AppDetail::getLastEvent)).setHeader("Last Event").setResizable(true);
        Column<AppDetail> lastEventActorColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.lastEventActor]]").withProperty("lastEventActor", AppDetail::getLastEventActor)).setHeader("Last Event Actor").setResizable(true);
        Column<AppDetail> lastEventTimeColumn = grid.addColumn(new LocalDateTimeRenderer<AppDetail>(AppDetail::getLastEventTime, dateTimeFormatter)).setHeader("Last Event Time").setTextAlign(ColumnTextAlign.END).setResizable(true);
        Column<AppDetail> requestedStateColumn = grid.addColumn(TemplateRenderer.<AppDetail> of("[[item.requestedState]]").withProperty("requestedState", AppDetail::getRequestedState)).setHeader("Requested State").setTextAlign(ColumnTextAlign.CENTER).setResizable(true);

        HeaderRow filterRow = grid.appendHeaderRow();

        TextField foundationField = new TextField();
        foundationField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getFoundation(), foundationField.getValue())));
        foundationField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(foundationColumn).setComponent(foundationField);
        foundationField.setSizeFull();
        foundationField.setPlaceholder("Filter");

        TextField organizationField = new TextField();
        organizationField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getOrganization(), organizationField.getValue())));
        organizationField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(organizationColumn).setComponent(organizationField);
        organizationField.setSizeFull();
        organizationField.setPlaceholder("Filter");

        TextField spaceField = new TextField();
        spaceField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getSpace(), spaceField.getValue())));
        spaceField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(spaceColumn).setComponent(spaceField);
        spaceField.setSizeFull();
        spaceField.setPlaceholder("Filter");

        TextField appIdField = new TextField();
        appIdField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getAppId(), appIdField.getValue())));
        appIdField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(appIdColumn).setComponent(appIdField);
        appIdField.setSizeFull();
        appIdField.setPlaceholder("Filter");

        TextField appNameField = new TextField();
        appNameField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getAppName(), appNameField.getValue())));
        appNameField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(appNameColumn).setComponent(appNameField);
        appNameField.setSizeFull();
        appNameField.setPlaceholder("Filter");

        TextField buildpackField = new TextField();
        buildpackField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getBuildpack(), buildpackField.getValue())));
        buildpackField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(buildpackColumn).setComponent(buildpackField);
        buildpackField.setSizeFull();
        buildpackField.setPlaceholder("Filter");

        TextField imageField = new TextField();
        imageField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getImage(), imageField.getValue())));
        imageField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(imageColumn).setComponent(imageField);
        imageField.setSizeFull();
        imageField.setPlaceholder("Filter");

        TextField stackField = new TextField();
        stackField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getStack(), stackField.getValue())));
        stackField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(stackColumn).setComponent(stackField);
        stackField.setSizeFull();
        stackField.setPlaceholder("Filter");

        TextField runningInstancesField = new TextField();
        runningInstancesField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.contains(String.valueOf(f.getRunningInstances()), runningInstancesField.getValue())));
        runningInstancesField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(runningInstancesColumn).setComponent(runningInstancesField);
        runningInstancesField.setSizeFull();
        runningInstancesField.setPlaceholder("Filter");

        TextField totalInstancesField = new TextField();
        totalInstancesField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.contains(String.valueOf(f.getTotalInstances()), totalInstancesField.getValue())));
        totalInstancesField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(totalInstancesColumn).setComponent(totalInstancesField);
        totalInstancesField.setSizeFull();
        totalInstancesField.setPlaceholder("Filter");

        TextField memUsedField = new TextField();
        memUsedField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.contains(String.valueOf(f.getMemoryUsage()), memUsedField.getValue())));
        memUsedField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(memUsedColumn).setComponent(memUsedField);
        memUsedField.setSizeFull();
        memUsedField.setPlaceholder("Filter");

        TextField diskUsedField = new TextField();
        diskUsedField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.contains(String.valueOf(f.getDiskUsage()), diskUsedField.getValue())));
        diskUsedField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(diskUsedColumn).setComponent(diskUsedField);
        diskUsedField.setSizeFull();
        diskUsedField.setPlaceholder("Filter");

        TextField urlsField = new TextField();
        urlsField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getUrlsAsCsv(), urlsField.getValue())));
        urlsField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(urlsColumn).setComponent(urlsField);
        urlsField.setSizeFull();
        urlsField.setPlaceholder("Filter");

        TextField lastPushedField = new TextField();
        lastPushedField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(dateTimeFormatter.format(f.getLastPushed()), lastPushedField.getValue())));
        lastPushedField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(lastPushedColumn).setComponent(lastPushedField);
        lastPushedField.setSizeFull();
        lastPushedField.setPlaceholder("Filter");

        TextField lastEventField = new TextField();
        lastEventField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getLastEvent(), lastEventField.getValue())));
        lastEventField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(lastEventColumn).setComponent(lastEventField);
        lastEventField.setSizeFull();
        lastEventField.setPlaceholder("Filter");

        TextField lastEventActorField = new TextField();
        lastEventActorField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getLastEventActor(), lastEventActorField.getValue())));
        lastEventActorField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(lastEventActorColumn).setComponent(lastEventActorField);
        lastEventActorField.setSizeFull();
        lastEventActorField.setPlaceholder("Filter");

        TextField lastEventTimeField = new TextField();
        lastEventTimeField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(dateTimeFormatter.format(f.getLastEventTime()), lastEventTimeField.getValue())));
        lastEventTimeField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(lastEventTimeColumn).setComponent(lastEventTimeField);
        lastEventTimeField.setSizeFull();
        lastEventTimeField.setPlaceholder("Filter");

        TextField requestedStateField = new TextField();
        requestedStateField.addValueChangeListener(
            event -> dataProvider.addFilter(
                f -> StringUtils.containsIgnoreCase(f.getRequestedState(), requestedStateField.getValue())));
        requestedStateField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(requestedStateColumn).setComponent(requestedStateField);
        requestedStateField.setSizeFull();
        requestedStateField.setPlaceholder("Filter");

        // @see https://github.com/vaadin/vaadin-grid-flow/issues/234
        for (Column<AppDetail> column : grid.getColumns())
	        column.getElement().getParent().callFunction("setAttribute", "resizable", true);

        return grid;
    }

}