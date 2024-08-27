import React, { useRef } from "react";
import { useCallback, useEffect, useState } from "react";
import Particles, { initParticlesEngine } from "@tsparticles/react";
import { loadSlim } from "@tsparticles/slim";

const MyParticles = (props) => {
  const {
    width = "100%",
    height = "100%",
    backgroundColor = "#D3D3D3",
    ...rest
  } = props;
  const canvasRef = useRef(null);
  const [context, setContext] = useState(null);
  const [stop, setStop] = useState(false);
  const pythonlength = 40;
 
  const draw = (frameCount) => {  
    const halfwidth = context.canvas.width / 2;
    const halfheight = context.canvas.height / 2;
    if(stop){
      context.fillStyle = "black";
          context.beginPath();
          context.arc(halfwidth+5, halfheight+18, 1, 0, 2 * Math.PI);
          context.fill();
          
          context.beginPath();
          context.arc(halfwidth-5, halfheight-18, 1, 0, 2 * Math.PI);
          context.fill();
    }
    if (!stop) {
      context.clearRect(0, 0, context.canvas.width, context.canvas.height);  
      context.lineWidth = 20;  
      if (frameCount >= halfheight - 50) {
        if (
          frameCount <= halfheight &&
          (halfheight - frameCount) > 20
        ) {

          context.strokeStyle = "#FFD700";
          context.beginPath();
          context.moveTo(
            halfwidth,
            context.canvas.height - frameCount
          ); // Move the pen to (30, 50)
          context.lineTo(halfwidth, halfheight); // Draw a line to (150, 100)
          context.moveTo(
            halfwidth,
            halfheight
          ); // Draw a line to (150, 100)
          context.lineTo(
            halfwidth +
              (50 - (halfheight - frameCount)),
            halfheight
          ); // Draw a line to (150, 100)
          context.stroke();


          context.strokeStyle = "#6495ED";
          context.beginPath();
          context.moveTo(halfwidth , frameCount); // Move the pen to (30, 50)
          context.lineTo(
            halfwidth,
            halfheight
          ); // Draw a line to (150, 100)
          context.moveTo(
            halfwidth-10,
            halfheight
          ); // Draw a line to (150, 100)
          context.lineTo(
            context.canvas.width / 2  -
              (50 - (context.canvas.height / 2 - frameCount)),
            context.canvas.height / 2
          ); // Draw a line to (150, 100)
          // context.lineTo(
          //   halfwidth  -
          //     (pythonlength - (halfheight - frameCount)),
          //   halfheight
          // ); // Draw a line to (150, 100)
          context.stroke();

          
          if (halfheight - (frameCount + 1) <= 20) {          
            setStop(true);
          }
        }
      } else {

        context.strokeStyle = "#6495ED";
        context.beginPath();
        context.moveTo(halfwidth, frameCount); // Move the pen to (30, 50)
        context.lineTo(halfwidth , frameCount + pythonlength); // Draw a line to (150, 100)
        context.stroke(); // Render the path

        context.strokeStyle = "#FFD700";
        context.beginPath();
        context.moveTo(
          halfwidth,
          context.canvas.height - frameCount
        ); // Move the pen to (30, 50)
        context.lineTo(
          halfwidth,
          context.canvas.height - (frameCount + pythonlength)
        ); // Draw a line to (150, 100)
      }
      context.stroke(); // Render the path
    }
  };
  useEffect(() => {
    //i.e. value other than null or undefined
    if (canvasRef.current) {
      const canvas = canvasRef.current;
      const ctx = canvas.getContext("2d");
      setContext(ctx);
    }
  }, []);
  useEffect(() => {
    let frameCount = 0;
    let animationFrameId;

    // Check if null context has been replaced on component mount
    if (context) {
      //Our draw came here
      const render = () => {
        frameCount += 0.4;
        const halfwidth = context.canvas.width / 2;
        const halfheight = context.canvas.height / 2;
        if (frameCount > context.canvas.height) {
          setStop(false);
          frameCount = 0;
        }
        draw(frameCount);
        animationFrameId = window.requestAnimationFrame(render);
      };
      render();
    }
    return () => {
      window.cancelAnimationFrame(animationFrameId);
    };
  }, [draw, context]);
  return (
    <canvas
      ref={canvasRef}
      {...props}
      style={{ position: "fixed", width, height, backgroundColor, zIndex: -1 }}
    />
  );
};

export default MyParticles;
