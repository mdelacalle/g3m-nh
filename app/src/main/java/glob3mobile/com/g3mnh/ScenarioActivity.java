package glob3mobile.com.g3mnh;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.DeviceAttitudeCameraHandler;
import org.glob3.mobile.generated.ElevationDataProvider;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GEO2DCoordinatesData;
import org.glob3.mobile.generated.GEO2DLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiPolygonGeometry;
import org.glob3.mobile.generated.GEO2DPointGeometry;
import org.glob3.mobile.generated.GEO2DPolygonGeometry;
import org.glob3.mobile.generated.GEOLineRasterSymbol;
import org.glob3.mobile.generated.GEOMarkSymbol;
import org.glob3.mobile.generated.GEOPolygonRasterSymbol;
import org.glob3.mobile.generated.GEORenderer;
import org.glob3.mobile.generated.GEOSymbol;
import org.glob3.mobile.generated.GEOSymbolizer;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SingleBilElevationDataProvider;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.URLTemplateLayer;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import java.util.ArrayList;

public class ScenarioActivity extends Activity {

    G3MBuilder_Android _builder;
    G3MWidget_Android _g3mWidget;
    private GEORenderer _vectorialRenderer;
    private GEO2DCoordinatesData _coordinates;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);

        String scenario = getIntent().getExtras().getString("SCENARIO");

        _builder = new G3MBuilder_Android(this);
        _builder.setAtmosphere(true);
        initializeScenario(scenario);

        //TODO: Uncomment for virtual visit
        //_builder.addPeriodicalTask(virtualVisit());
        _g3mWidget = _builder.createWidget();

        //TODO: Uncomment for Stereo
        // _g3mWidget.getG3MWidget().setViewMode(ViewMode.STEREO);

        //TODO: Uncomment for VR
        // activateVRSensors();

        layout = (RelativeLayout) findViewById(R.id.glob3);

        Geodetic3D correctedPosition = new Geodetic3D(Angle.fromDegrees(46.059), Angle.fromDegrees(11.1173), 5000);
        _g3mWidget.setCameraPosition(correctedPosition);

        // _g3mWidget.getG3MWidget().setAnimatedCameraPosition(TimeInterval.fromSeconds(3), correctedPosition, Angle.fromDegrees(0), Angle.fromDegrees(-60D), false);

        layout.addView(_g3mWidget);

    }

    private void initializeScenario(String scenario) {


        final Geodetic2D lower = new Geodetic2D( //
                Angle.fromDegrees(45.941), //
                Angle.fromDegrees(10.918));
        final Geodetic2D upper = new Geodetic2D( //
                Angle.fromDegrees(46.311), //
                Angle.fromDegrees(11.276));

        final Sector demSector = new Sector(lower, upper);

        //  final double deltaHeight = -700.905;
        final double deltaHeight = 0;
        final float verticalExaggeration = 1.5f;
        final ElevationDataProvider elevationDataProvider = new SingleBilElevationDataProvider(new URL("file:///trentinoLR.bil", false),
                demSector, new Vector2I(2000, 2000), deltaHeight);

        LayerSet layerSet = new LayerSet();

        final URLTemplateLayer imageryLayer = URLTemplateLayer.newMercator("http://b.tiles.mapbox.com/v4/bobbysud.79c006a5/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiYm9iYnlzdWQiLCJhIjoiTi16MElIUSJ9.Clrqck--7WmHeqqvtFdYig",
                Sector.fullSphere(), true, 2, 18, TimeInterval.fromDays(30), true, 1);
        imageryLayer.setTitle("Mapbox imagery layer");
        imageryLayer.setEnable(true);
        layerSet.addLayer(imageryLayer);
        _builder.getPlanetRendererBuilder().setLayerSet(layerSet);
        _builder.getPlanetRendererBuilder().setElevationDataProvider(elevationDataProvider);
        _builder.getPlanetRendererBuilder().setVerticalExaggeration(verticalExaggeration);

        _vectorialRenderer = _builder.createGEORenderer(Symbolizer);
        _vectorialRenderer.loadJSON(new URL("file:///pois.geojson"));


        if (scenario.equals("RUN_SCENARIO_EASY")) {
            _vectorialRenderer.loadJSON(new URL("file:///route_run_easy.geojson"));
             showInfo("#", "Carrera sencilla por la ciudad, sin desnivel apreciable recorriendo el río Adigio y entrando hacia la ciudad", ContextCompat.getDrawable(ScenarioActivity.this, R.drawable.run_2));
        }
        if (scenario.equals("RUN_SCENARIO_HARD")) {
            _vectorialRenderer.loadJSON(new URL("file:///route_run_hard.geojson"));
        }
        if (scenario.equals("WALKING_CITY_CULTURE")) {
            _vectorialRenderer.loadJSON(new URL("file:///walking_city_culture.geojson"));
        }
        if (scenario.equals("WALKING_CITY_ADVENTURE")) {
            _vectorialRenderer.loadJSON(new URL("file:///walking_city_adventure.geojson"));
        }


    }

    private void showInfo(String distance, String description, Drawable drawable ){

        RelativeLayout container = (RelativeLayout)findViewById(R.id.info_container);
        container.bringToFront();

        ImageView infoImage = (ImageView)container.findViewById(R.id.infoImage);
        infoImage.setImageDrawable(drawable);

        TextView distanceTW = (TextView) findViewById(R.id.distance);
        distanceTW.setText(distance);

        TextView descriptionTW = (TextView) findViewById(R.id.description);
        descriptionTW.setText(description);

    }

    private PeriodicalTask virtualVisit() {
        final PeriodicalTask periodicalTask = new PeriodicalTask(TimeInterval.fromSeconds(3), new GTask() {

            int i = 0;

            @Override
            public void run(final G3MContext context) {

                if (_coordinates != null) {
                    Geodetic2D coordinate = null;
                    if (i < _coordinates.size()) {
                        coordinate = _coordinates.get(i);
                    }
                    _g3mWidget.getG3MWidget().setAnimatedCameraPosition(TimeInterval.fromSeconds(3), new Geodetic3D(coordinate, 2000), Angle.fromDegrees(0), Angle.fromDegrees(-60D), false);
                    i++;
                }


            }
        });
        return periodicalTask;
    }


    private void activateVRSensors() {
        CameraRenderer cameraRenderer = _g3mWidget.getCameraRenderer();
        cameraRenderer.removeAllHandlers(true);
        cameraRenderer.addHandler(new DeviceAttitudeCameraHandler(false));
    }

    GEOSymbolizer Symbolizer = new GEOSymbolizer() {

        @Override
        public ArrayList<GEOSymbol> createSymbols(final GEO2DMultiPolygonGeometry geometry) {
            // TODO Auto-generated method stub
            return null;
        }


        //@Override
        public ArrayList<GEOSymbol> createSymbols(final GEO2DPolygonGeometry geometry) {
            final ArrayList<GEOSymbol> symbols = new ArrayList<GEOSymbol>();
            symbols.add(new GEOPolygonRasterSymbol(geometry.getPolygonData(), Symbology.createPolygonLineRasterStyle(geometry),
                    Symbology.createPolygonSurfaceRasterStyle(geometry)));

            return symbols;
        }


        @Override
        public ArrayList<GEOSymbol> createSymbols(final GEO2DMultiLineStringGeometry geometry) {
            // TODO Auto-generated method stub
            return null;
        }


        @Override
        public ArrayList<GEOSymbol> createSymbols(final GEO2DLineStringGeometry geometry) {
            final ArrayList<GEOSymbol> symbols = new ArrayList<GEOSymbol>();
            _coordinates = geometry.getCoordinates();
            symbols.add(new GEOLineRasterSymbol(geometry.getCoordinates(), Symbology.createLineRasterStyle(geometry)));

            return symbols;
        }


        @Override
        public ArrayList<GEOSymbol> createSymbols(final GEO2DPointGeometry geometry) {

            final ArrayList<GEOSymbol> result = new ArrayList<GEOSymbol>();

            final JSONObject properties = geometry.getFeature().getProperties();

            final String name = properties.getAsString("desc", "");


            final Mark mark = new Mark( //
                    new URL("file:///ng_logo.png"), //
                    new Geodetic3D(geometry.getPosition(), 0), //
                    AltitudeMode.RELATIVE_TO_GROUND, //
                    5000, //
                    null, //
                    false, //
                    null, //
                    true);


            result.add(new GEOMarkSymbol(mark));
            return result;

        }
    };


}
