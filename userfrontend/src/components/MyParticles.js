import { ContentCopyRounded } from "@mui/icons-material";
import React, { useRef } from "react";
import { useEffect, useState } from "react";

const MyParticles = (props) => {
  const {
    width = "100%",
    height = "100%",
    backgroundColor = "#D3D3D3",
    ...rest
  } = props;

  const pythonlength = 20;
  const canvasRef = useRef(null);
  const [context, setContext] = useState(null);
  const step = 0.2;
  var frameCount = 0;
  //show moment with snakes
  var wait = false;
  //tu small in background
  const points = [
    [20, 20],
    [70, 70],
    [130, 200],
    [210, 250],
  ];
  const sizes = [40, 70, 80, 40];

  const draw = (frameCount) => {

    const halfwidth = context.canvas.width / 2;
    const halfheight = context.canvas.height / 2;
    context.lineWidth = 10;
    const lineWidth = 10;
    const between = 0.75 * context.lineWidth;

    if (wait) {
      
      let wyzn = halfheight - pythonlength;
      //zolty i co dalej
     
        if(frameCount>context.canvas.height / 2 + 0.5* pythonlength)
        {
          
          context.strokeStyle = "white"; 
          context.beginPath();
          context.moveTo(
            halfwidth,
            halfheight +lineWidth/2
          );
          context.lineTo(
            halfwidth + between,
            halfheight + lineWidth/2
          );
          context.lineWidth = 1;
          context.stroke();

          context.beginPath();
          context.moveTo(
            halfwidth,
            halfheight -lineWidth/2
          );
          context.lineTo(
            halfwidth - between,
            halfheight - lineWidth/2
          );
          context.stroke();   

          context.strokeStyle = "#FFD700"; 
          context.beginPath();
          context.moveTo(
            halfwidth,
            halfheight +lineWidth/2+1
          );
          context.lineTo(
            halfwidth + between,
            halfheight + lineWidth/2 +1
          );
          context.lineWidth = 1;
          context.stroke();
          
          context.strokeStyle ="#6495ED"
          context.beginPath();
          context.moveTo(
            halfwidth,
            halfheight -lineWidth/2-1
          );
          context.lineTo(
            halfwidth - between,
            halfheight - lineWidth/2 -1
          );
          context.stroke();   
          
        }
        
        else if(frameCount > wyzn) {

          context.strokeStyle = "#FFD700";
          context.beginPath();
          context.moveTo(
            halfwidth - between,
            halfheight + context.lineWidth / 2
          );
          context.lineTo(
            halfwidth + between,
            halfheight + context.lineWidth / 2
          );
          context.stroke();

          context.strokeStyle = "#6495ED";
          context.beginPath();
          context.moveTo(
            halfwidth - between,
            halfheight - context.lineWidth / 2
          );
          context.lineTo(
            halfwidth + between,
            halfheight - context.lineWidth / 2
          );
          context.stroke();
        
        if (frameCount < wyzn + 2 * context.lineWidth) {
          
          context.fillStyle = "white";
          
          //zolty 
          context.beginPath();
          context.arc(
            halfwidth - between + context.lineWidth / 4,
            frameCount + 0.75 * context.lineWidth,
            1,
            0,  
            2 * Math.PI
          );
          context.fill();

          //niebieski
          context.beginPath();
          context.arc(
            halfwidth + between - context.lineWidth / 4,
            halfheight - context.lineWidth / 4-(frameCount-halfheight+0.5*context.lineWidth),
            1,
            0,
            2 * Math.PI
          );
          context.fill();
        
        } else {
          //to jest akurat oczko przesuwajace sie w prawo
          context.fillStyle = "white";
          console.log("jestem");
          context.beginPath();
          context.arc(
            halfwidth -
              between +
              context.lineWidth / 4 +
              frameCount -
              (wyzn + 2.0 * context.lineWidth),
            wyzn + 2.75 * context.lineWidth,
            1,
            0,
            2 * Math.PI
          );
          context.fill();

           //to jest akurat oczko przesuwajace sie w lewo
           context.fillStyle = "white";
           console.log("jestem");
           context.beginPath(); 
           context.arc(
             halfwidth +
               between -
               context.lineWidth / 4 -
               (frameCount -
               (wyzn + 2.0 * context.lineWidth)),
               halfheight - 0.75*context.lineWidth,
             1,
             0,
             2 * Math.PI
           );
           context.fill();
        }
      } else {
        context.fillStyle = "white";
        context.beginPath();
        context.arc(
          halfwidth +
            between -
            (pythonlength - context.lineWidth / 2) -
            context.lineWidth / 4,
          frameCount + context.lineWidth / 2,
          1,
          0,
          2 * Math.PI
        );
        context.fill();

        context.beginPath();
        context.arc(
          halfwidth -
            between +
            (pythonlength - (context.lineWidth / 2 + 1)) -
            context.lineWidth / 4,
          halfheight - context.lineWidth / 4,
          1,
          0,
          2 * Math.PI
        );
        context.fill();
      }
    } else {
      
      context.clearRect(0, 0, context.canvas.width, context.canvas.height);

      if (frameCount >= halfheight - pythonlength) {
        if (
          frameCount <= halfheight &&
          halfheight - frameCount > context.lineWidth / 2
        ) {
          //zolty
          context.strokeStyle = "#FFD700";
          context.beginPath();
          context.moveTo(halfwidth + between, frameCount);
          context.lineTo(
            halfwidth + between,
            halfheight + context.lineWidth / 2
          );
          context.stroke();

          //niebieski
          context.strokeStyle = "#6495ED";
          context.beginPath();
          context.moveTo(
            halfwidth - between,
            halfheight - context.lineWidth / 2
          );
          context.lineTo(
            halfwidth - between + (pythonlength - (halfheight - frameCount)),
            halfheight - context.lineWidth / 2
          );
          context.stroke();

          //niebieski
          context.strokeStyle = "#6495ED";
          context.beginPath();
          context.moveTo(
            halfwidth - between,
            context.canvas.height - frameCount
          );
          context.lineTo(
            halfwidth - between,
            halfheight - context.lineWidth / 2
          );
          context.stroke();

          //zolty
          context.strokeStyle = "#FFD700";
          context.beginPath();
          context.moveTo(
            halfwidth + between,
            halfheight + context.lineWidth / 2
          );
          context.lineTo(
            halfwidth + between - (pythonlength - (halfheight - frameCount)),
            halfheight + context.lineWidth / 2
          );
          context.stroke();

          //eyes yellow
          context.fillStyle = "white";
          context.beginPath();
          context.arc(
            halfwidth +
              between -
              (pythonlength - (halfheight - frameCount)) +
              context.lineWidth / 4,

            halfheight + context.lineWidth / 4,
            1,
            0,
            2 * Math.PI
          );
          context.fill();

          //eyes blue
          context.beginPath();
          context.arc(
            halfwidth -
              between +
              (pythonlength - (halfheight - frameCount)) -
              context.lineWidth / 4,
            halfheight - context.lineWidth / 4,
            1,
            0,
            2 * Math.PI
          );
          context.fill();

          if (halfheight - (frameCount + step) <= context.lineWidth / 2) {
            wait = true;
          }
        }
      } else {

        context.strokeStyle = "#6495ED";
        context.beginPath();
        context.moveTo(halfwidth - between, context.canvas.height - frameCount);
        context.lineTo(
          halfwidth - between,
          context.canvas.height - (frameCount + pythonlength)
        );
        context.stroke();

        //snakes are falling down
        context.strokeStyle = "#FFD700";
        context.beginPath();
        context.moveTo(halfwidth + between, frameCount);
        context.lineTo(halfwidth + between, frameCount + pythonlength);
        context.stroke();

        //eyes yellow
        context.fillStyle = "white";
        context.beginPath();
        context.arc(
          halfwidth + between - context.lineWidth / 4,
          frameCount + pythonlength - 5,
          1,
          0,
          2 * Math.PI
        );
        context.fill();

        //eyes blue
        context.beginPath();
        context.arc(
          halfwidth - between + context.lineWidth / 4,
          context.canvas.height - (frameCount + pythonlength) + 5,
          1,
          0,
          2 * Math.PI
        );
        context.fill();
      }
    }
  };

  useEffect(() => {
    if (canvasRef.current) {
      const canvas = canvasRef.current;
      const ctx = canvas.getContext("2d");
      setContext(ctx);
    }
  });

  useEffect(() => {
    let animationFrameId;

    if (context) {
      const render = () => {
        frameCount += step;

        if (frameCount > context.canvas.height / 2 + 1.5*pythonlength) {
          wait = false;
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
