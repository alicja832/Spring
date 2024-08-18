 
    import React from 'react';
    import { useCallback, useEffect, useState } from "react";
    import Particles, { initParticlesEngine }  from "@tsparticles/react";
    import { loadSlim } from "@tsparticles/slim";
    
    export default function MyParticles() {
      const [ init, setInit ] = useState(false);
    
      // this should be run only once per application lifetime
      useEffect(() => {
          initParticlesEngine(async (engine) => {
              // you can initiate the tsParticles instance (engine) here, adding custom shapes or presets
              // this loads the tsparticles package bundle, it's the easiest method for getting everything ready
              // starting from v2 you can add only the features you need reducing the bundle size
              //await loadAll(engine);
              //await loadFull(engine);
              await loadSlim(engine);
              //await loadBasic(engine);
          }).then(() => {
              setInit(true);
          });
      }, []);
    
      const particlesLoaded = (container) => {
          console.log(container);
      };  
      return(
    <Particles
    id="tsparticles"
    init={initParticlesEngine}
    loaded={particlesLoaded}
    options={{ "fullScreen": true, "background":{ "image":" linear-gradient(19deg, #21D4FD 0%, #3904fd 100%)" },
     "particles":{ "number":{ "value":10, "density":{ "enable":false, "value_area":100 } }, 
     "color":{ "value":"#ffffff" }, 
     "shape": { "type": "square", "stroke":{ "width":0, "color":"#000000" },
     "polygon":{ "nb_sides":5 } }, 
     "opacity":{ "value":0.25, 
     "random":false, 
     "anim":{ "enable":false, "speed":1, "opacity_min":0.1, "sync":false } }, 
     "size":{ "value":29, "random":false, "anim":{ "enable":false, "speed":2, "size_min":0.1, "sync":false } }, 
     "line_linked":{ "enable":false, "distance":300, "color":"#ffffff", "opacity":0, "width":0 }, 
     "move":{ "enable":true, "speed":0.5, "direction":"center", "straight":true, "out_mode":"out", "bounce":false,
     "attract":{ "enable":false, "rotateX":600, "rotateY":600 } } }, 
     "interactivity":{ "detect_on":"canvas", "events":{ "onhover":{ "enable":false, "mode":"repulse" }, 
     "onclick":{ "enable":false, "mode":"push" }, "resize":true }, 
     "modes":{ "grab":{ "distance":800, "line_linked":{ "opacity":1 } }, 
     "bubble":{ "distance":790, "size":79, "duration":2, "opacity":0.8, "speed":3 }, 
     "repulse":{ "distance":400, "duration":0.4 }, "push":{ "particles_nb":4 },
    "remove":{ "particles_nb":2 } } }, "retina_detect":true}}
    style={{
      position: "absolute",
      top: 0,
      left: 0,
      width: "100%",
      height: "100%",
      zIndex: -1 // This ensures particles stay in the background
    }}
      />
);
}