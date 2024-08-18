import React from 'react';
import { useCallback, useEffect, useState } from "react";
import Particles, { initParticlesEngine }  from "@tsparticles/react";
import { loadSlim } from "@tsparticles/slim";
import MyParticles from './MyParticles';
export default function Home() {
  const [ init, setInit ] = useState(false);

  // this should be run only once per application lifetime
 
  return(
  <div>
   <MyParticles></MyParticles>
    {/* Text on top of particles */}
    <div id = "sthelse" style={{
        position: "relative",
        color: "black",  // Text color
        textAlign: "center",
      }}>
    <h2>Home</h2>
     <p>Welcome to the Python learning application.</p>
    </div>
      </div>
  )
};

