USE [ShellPest]
GO

/****** Object:  StoredProcedure [dbo].[SP_Pais_Select]    Script Date: 03/12/2020 01:45:14 p. m. ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF  EXISTS (SELECT * FROM SYS.OBJECTS WHERE TYPE = 'P' AND NAME = 'SP_WS_Control_Cant_Combustible_Select')
DROP PROCEDURE SP_WS_Control_Cant_Combustible_Select
GO
	
CREATE PROCEDURE SP_WS_Control_Cant_Combustible_Select	
@c_codigo_eps char(2),
@Id_Huerta char(5),
@v_tipo_gas varchar(60)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	
		select IT.v_cantingreso_gas - CT.v_cantutilizada_gas as  Cant_Conbustible
		from 
		(select  isnull(sum(isnull(I.v_cantingreso_gas,0)),0) as v_cantingreso_gas 
			from t_Gasolina_Ingreso as I 
			where I.c_codigo_eps =@c_codigo_eps 
			and I.Id_Huerta =@Id_Huerta
			and rtrim(ltrim(I.v_tipo_gas)) =rtrim(ltrim(@v_tipo_gas)) ) as IT
		left join ( 
			select  isnull(sum(isnull(C.v_cantutilizada_gas,0)),0) as v_cantutilizada_gas 
			from t_Gasolina_Consumo as C 
			where C.c_codigo_eps =@c_codigo_eps 
			and C.Id_Huerta =@Id_Huerta
			and rtrim(ltrim(C.v_tipo_gas)) =rtrim(ltrim(@v_tipo_gas)) ) as CT
		on 1=1

END


GO

